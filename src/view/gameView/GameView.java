package view.gameView;

import controller.GameController;
import model.*;
import model.game.Game;
import model.player.Player;
import model.trap.Trap;
import view.gameView.GameLogPanel; // Assurez-vous d'importer le nouveau panel
import view.gameView.GridPanel;
import view.gameView.InfoPanel;
import view.gameView.WeaponPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class GameView extends JFrame implements GameListener, IslandListener {

    private final GameController controller;
    private final Game game;

    // --- SOUS-COMPOSANTS ---
    private final GridPanel playerGridPanel;
    private final GridPanel opponentGridPanel;
    private final InfoPanel infoPanel;
    private final WeaponPanel weaponPanel;

    // Composants du bas
    private final JLabel statusLabel;
    private final GameLogPanel logPanel;

    private Trap pendingTrapToPlace = null;

    public GameView(GameController controller, Game game) {
        this.controller = controller;
        this.game = game;

        this.game.addListener(this);
        this.game.addIslandListener(this);

        setTitle("Bataille Navale - En Jeu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        this.weaponPanel = new WeaponPanel(controller);
        add(weaponPanel, BorderLayout.WEST);

        this.infoPanel = new InfoPanel(game, this);
        add(infoPanel, BorderLayout.EAST);

        boolean islandMode = controller.getGameConfiguration().isIslandMode();

        this.playerGridPanel = new GridPanel(
                game.getHumanPlayer().getOwnGrid(),
                islandMode,
                this::onPlayerGridClick
        );

        this.opponentGridPanel = new GridPanel(
                game.getM_computerPlayer().getOwnGrid(),
                islandMode,
                this::onOpponentGridClick
        );

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        centerPanel.add(wrapGrid(playerGridPanel, "MA FLOTTE"));
        centerPanel.add(wrapGrid(opponentGridPanel, "RADAR (ATTAQUEZ ICI)"));
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        this.statusLabel = new JLabel("Initialisation de la partie...", SwingConstants.CENTER);
        this.statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.statusLabel.setOpaque(true);
        this.statusLabel.setBackground(new Color(230, 230, 230));
        this.statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        this.logPanel = new GameLogPanel();

        bottomPanel.add(this.statusLabel, BorderLayout.NORTH);
        bottomPanel.add(this.logPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        updateGrids();

        logPanel.addLog("Bienvenue Capitaine ! La bataille commence.");
    }

    private JPanel wrapGrid(GridPanel grid, String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private void onPlayerGridClick(Coordinate coord) {
        if (this.pendingTrapToPlace != null) {
            boolean failed = controller.handlePlaceTrap(this.pendingTrapToPlace, coord);
            if (!failed) {
                this.pendingTrapToPlace = null;
                setStatus("Piège placé. À l'attaque !");
            }
        } else {
            setStatus("C'est votre flotte. Cliquez à droite pour attaquer.");
        }
    }

    private void onOpponentGridClick(Coordinate coord) {
        if (this.pendingTrapToPlace != null) {
            setStatus("ACTION REQUISE : Placez d'abord le piège à GAUCHE !");
            logPanel.addLog("Erreur : Vous essayez d'attaquer avant de placer votre piège.");
            Toolkit.getDefaultToolkit().beep();
        } else {
            controller.handleHumanAttack(coord.getX(), coord.getY());
        }
    }

    public void showScreen() {
        this.setVisible(true);
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
        if (text.toLowerCase().contains("erreur") || text.toLowerCase().contains("impossible")) {
            statusLabel.setForeground(Color.RED);
        } else {
            statusLabel.setForeground(Color.BLACK);
        }
    }

    public void updateGrids() {
        playerGridPanel.updateGridDisplay(true);
        opponentGridPanel.updateGridDisplay(false);
        infoPanel.updateStats();
    }

    public void setInputEnabled(boolean enabled) {
        playerGridPanel.setInputEnabled(enabled);
        opponentGridPanel.setInputEnabled(enabled);
    }

    @Override
    public void turnEnded(Player player, Map<Coordinate, String> moves) {
        logPanel.addLog("--- Fin du tour " + game.getTurnNumber() + " ---");
        updateGrids();
    }

    @Override
    public void onCellUpdated(Player p, Coordinate c) {
        updateGrids();
        if (p.equals(game.getHumanPlayer()) && !game.isGameOver()) {
            setInputEnabled(true);
            setStatus("À vous de jouer ! Choisissez une cible.");
        }
    }

    @Override
    public void onShipSunk(Player defender) {
        String msg = "NAVIRE COULÉ ! La flotte de " + defender.getNickName() + " perd un bâtiment.";
        setStatus(msg);
        logPanel.addLog(msg, true);
        updateGrids();
    }

    @Override
    public void onGameOver(Player winner) {
        setInputEnabled(false);
        String msg = "VICTOIRE DE " + winner.getNickName() + " !";
        setStatus(msg);
        logPanel.addLog(msg, true);
        JOptionPane.showMessageDialog(this, msg, "Fin de Partie", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onScanResult(Player player, List<Coordinate> scannedArea, List<ScanResult> results) {
        String msg = "Sonar utilisé par " + player.getNickName() + " : " + results.size() + " échos détectés.";
        logPanel.addLog(msg);
        setStatus(msg);
        updateGrids();

        if (player == game.getM_computerPlayer()) {
            setInputEnabled(true);
            setStatus("L'ennemi a scanné. À vous !");
        }
    }
    @Override
    public void notifyPlaceIslandEntity(Trap entity, Player player) {
        if (player == game.getM_computerPlayer()) {
            logPanel.addLog("L'adversaire a trouvé un " + entity.getType() + " sur l'île !");
            return;
        }

        this.pendingTrapToPlace = entity;
        setInputEnabled(true);
        String msg = "TRÉSOR ! Vous avez trouvé : " + entity.getType();
        logPanel.addLog(msg, true);
        setStatus("Placez votre " + entity.getType() + " sur votre grille (Gauche).");
    }

    @Override
    public void notifyTrapWrongPlacement() {
        Toolkit.getDefaultToolkit().beep();
        logPanel.addLog("Erreur de placement : Zone invalide (occupée ou île).");
        setStatus("Erreur : Impossible de placer ici.");
    }

    @Override
    public void notifyWeaponFind(EntityType weaponType) {
        String msg = "NOUVELLE ARME : " + weaponType + " ajoutée à l'arsenal !";
        logPanel.addLog(msg, true);
        setStatus(msg);
        infoPanel.updateStats();
    }

    @Override
    public void onBlackHolHit(Player defender){
        logPanel.addLog(defender.getName() + "a touché un trou noir l'attaque à été repercuté sur ça grille");
    }

    @Override
    public void onStormHit(Player attacker){
        logPanel.addLog(attacker.getName() + "a touché une Tornade");
    }


}