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

    private final GameController m_controller;
    private final Game m_game;

    // --- SOUS-COMPOSANTS ---
    private final GridPanel m_playerGridPanel;
    private final GridPanel m_opponentGridPanel;
    private final InfoPanel m_infoPanel;
    private final WeaponPanel m_weaponPanel;

    // Composants du bas
    private final JLabel m_statusLabel;
    private final GameLogPanel m_logPanel;

    private Trap m_pendingTrapToPlace = null;

    public GameView(GameController controller, Game game) {
        this.m_controller = controller;
        this.m_game = game;

        this.m_game.addListener(this);
        this.m_game.addIslandListener(this);

        setTitle("Bataille Navale - En Jeu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        this.m_weaponPanel = new WeaponPanel(controller);
        add(m_weaponPanel, BorderLayout.WEST);

        this.m_infoPanel = new InfoPanel(game, this);
        add(m_infoPanel, BorderLayout.EAST);

        boolean islandMode = controller.getGameConfiguration().isIslandMode();

        this.m_playerGridPanel = new GridPanel(
                game.getHumanPlayer().getOwnGrid(),
                islandMode,
                this::onPlayerGridClick
        );

        this.m_opponentGridPanel = new GridPanel(
                game.getComputerPlayer().getOwnGrid(),
                islandMode,
                this::onOpponentGridClick
        );

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        centerPanel.add(wrapGrid(m_playerGridPanel, "MA FLOTTE"));
        centerPanel.add(wrapGrid(m_opponentGridPanel, "RADAR (ATTAQUEZ ICI)"));
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        this.m_statusLabel = new JLabel("Initialisation de la partie...", SwingConstants.CENTER);
        this.m_statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.m_statusLabel.setOpaque(true);
        this.m_statusLabel.setBackground(new Color(230, 230, 230));
        this.m_statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        this.m_logPanel = new GameLogPanel();

        bottomPanel.add(this.m_statusLabel, BorderLayout.NORTH);
        bottomPanel.add(this.m_logPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        updateGrids();

        m_logPanel.addLog("Bienvenue Capitaine ! La bataille commence.");
    }

    private JPanel wrapGrid(GridPanel grid, String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private void onPlayerGridClick(Coordinate coord) {
        if (this.m_pendingTrapToPlace != null) {
            boolean failed = m_controller.handlePlaceTrap(this.m_pendingTrapToPlace, coord);
            if (!failed) {
                this.m_pendingTrapToPlace = null;
                setStatus("Piège placé. À l'attaque !");
            }
        } else {
            setStatus("C'est votre flotte. Cliquez à droite pour attaquer.");
        }
    }

    private void onOpponentGridClick(Coordinate coord) {
        if (this.m_pendingTrapToPlace != null) {
            setStatus("ACTION REQUISE : Placez d'abord le piège à GAUCHE !");
            m_logPanel.addLog("Erreur : Vous essayez d'attaquer avant de placer votre piège.");
            Toolkit.getDefaultToolkit().beep();
        } else {
            m_controller.handleHumanAttack(coord.getX(), coord.getY());
        }
    }

    public void showScreen() {
        this.setVisible(true);
    }

    public void setStatus(String text) {
        m_statusLabel.setText(text);
        if (text.toLowerCase().contains("erreur") || text.toLowerCase().contains("impossible")) {
            m_statusLabel.setForeground(Color.RED);
        } else {
            m_statusLabel.setForeground(Color.BLACK);
        }
    }

    public void updateGrids() {
        m_playerGridPanel.updateGridDisplay(true);
        m_opponentGridPanel.updateGridDisplay(false);
        m_infoPanel.updateStats();
    }

    public void setInputEnabled(boolean enabled) {
        m_playerGridPanel.setInputEnabled(enabled);
        m_opponentGridPanel.setInputEnabled(enabled);
    }

    @Override
    public void turnEnded(Player player, Map<Coordinate, String> moves) {
        m_logPanel.addLog("--- Fin du tour " + m_game.getTurnNumber() + " ---");
        updateGrids();
    }

    @Override
    public void onCellUpdated(Player p, Coordinate c) {
        updateGrids();
        if (p.equals(m_game.getHumanPlayer()) && !m_game.isGameOver()) {
            setInputEnabled(true);
            setStatus("À vous de jouer ! Choisissez une cible.");
        }
    }

    @Override
    public void onShipSunk(Player defender) {
        String msg = "NAVIRE COULÉ ! La flotte de " + defender.getNickName() + " perd un bâtiment.";
        setStatus(msg);
        m_logPanel.addLog(msg, true);
        updateGrids();
    }

    @Override
    public void onGameOver(Player winner) {
        this.setInputEnabled(false);
        this.setStatus("PARTIE TERMINÉE ! Vainqueur: " + winner.getNickName());
        Toolkit.getDefaultToolkit().beep();
        int choice = JOptionPane.showConfirmDialog(
                this,
                "La partie est terminée !\n" +
                        "Vainqueur : " + winner.getNickName() + "\n\n" +
                        "Souhaitez-vous rejouer ?",
                "Fin de Partie",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            m_controller.restartGame();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onScanResult(Player player, List<Coordinate> scannedArea, List<ScanResult> results) {
        String msg = "Sonar utilisé par " + player.getNickName() + " : " + results.size() + " échos détectés.";
        m_logPanel.addLog(msg);
        setStatus(msg);
        updateGrids();

        if (player == m_game.getComputerPlayer()) {
            setInputEnabled(true);
            setStatus("L'ennemi a scanné. À vous !");
        }
    }
    @Override
    public void notifyPlaceIslandEntity(Trap entity, Player player) {
        if (player == m_game.getComputerPlayer()) {
            m_logPanel.addLog("L'adversaire a trouvé un " + entity.getType() + " sur l'île !");
            player.placeFoundTrap(entity, null, player.getOwnGrid());
            return;
        }

        this.m_pendingTrapToPlace = entity;
        setInputEnabled(true);
        String msg = "TRÉSOR ! Vous avez trouvé : " + entity.getType();
        m_logPanel.addLog(msg, true);
        setStatus("Placez votre " + entity.getType() + " sur votre grille (Gauche).");
    }

    @Override
    public void notifyTrapWrongPlacement() {
        Toolkit.getDefaultToolkit().beep();
        m_logPanel.addLog("Erreur de placement : Zone invalide (occupée ou île).");
        setStatus("Erreur : Impossible de placer ici.");
    }

    @Override
    public void notifyWeaponFind(EntityType weaponType) {
        String msg = "NOUVELLE ARME : " + weaponType + " ajoutée à l'arsenal !";
        m_logPanel.addLog(msg, true);
        setStatus(msg);
        m_infoPanel.updateStats();
    }

    @Override
    public void onBlackHolHit(Player defender){
        m_logPanel.addLog(defender.getName() + "a touché un trou noir l'attaque à été repercuté sur ça grille");
    }

    @Override
    public void onStormHit(Player attacker){
        m_logPanel.addLog(attacker.getName() + "a touché une Tornade");
    }
}