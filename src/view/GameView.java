package view;

import controller.GameController;
import model.Coordinate;
import model.GameListener;
import model.ScanResult;
import model.game.Game;
import model.map.Cell;
import model.map.Grid;
import model.player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class GameView extends JFrame implements GameListener {

    private final GameController controller;
    private final Game game;
    private final int gridSize;

    private final JPanel[][] playerCells;
    private final JPanel[][] opponentCells;

    private final JLabel statusLabel;
    private final JLabel turnLabel;
    private JPanel infoPanel;
    private ButtonGroup weaponGroup;

    private boolean inputEnabled = true;

    public GameView(GameController controller, Game game) {
        this.controller = controller;
        this.game = game;
        this.game.addListener(this);
        this.gridSize = game.getHumanPlayer().getGridSize();

        this.playerCells = new JPanel[gridSize][gridSize];
        this.opponentCells = new JPanel[gridSize][gridSize];
        this.turnLabel = new JLabel("Tour : 1", SwingConstants.CENTER);

        setTitle("Bataille Navale - En Jeu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(createInfoPanel(), BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createDualGridPanel(), BorderLayout.CENTER);
        centerPanel.add(this.turnLabel, BorderLayout.NORTH);

        add(createWeaponPanel(), BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

        this.statusLabel = new JLabel("À vous de jouer ! Cliquez sur la grille de droite.", SwingConstants.CENTER);
        this.statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(this.statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        updateGrids();
    }

    private JPanel createInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Statistiques"));
        infoPanel.setPreferredSize(new Dimension(300, 0));
        updateInfoPanel();
        return infoPanel;
    }

    private void updateInfoPanel() {
        infoPanel.removeAll();
        Player human = game.getHumanPlayer();
        Player computer = game.getM_computerPlayer();

        infoPanel.add(new JLabel("<html><h3>Tour " + game.getTurnNumber() + "</h3></html>"));
        infoPanel.add(Box.createVerticalStrut(10));

        infoPanel.add(new JLabel("<html><b>--- Capitaine " + human.getNickName() + " (Vous) ---</b></html>"));
        infoPanel.add(createPlayerStats(human, computer));
        infoPanel.add(Box.createVerticalStrut(10));

        infoPanel.add(new JLabel("<html><b>--- Adversaire (IA) ---</b></html>"));
        infoPanel.add(createPlayerStats(computer, human));

        infoPanel.add(Box.createVerticalStrut(20));
        JButton historyBtn = new JButton("Voir l'historique complet");
        historyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyBtn.addActionListener(e -> showHistory());
        infoPanel.add(historyBtn);

        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private void showHistory() {
        String historyLog = game.getHistory();
        JTextArea textArea = new JTextArea(historyLog);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Historique de la partie", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createPlayerStats(Player targetPlayer, Player opponentPlayer) {
        JPanel stats = new JPanel(new GridLayout(0, 1));
        stats.setBorder(new EmptyBorder(5, 10, 5, 10));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);

        Map<String, Integer> shipStatus = targetPlayer.getShipStatusStats();
        Map<String, Integer> hitStats = opponentPlayer.getHitAccuracyStats(targetPlayer);

        stats.add(new JLabel("Bateaux intacts/touchés/coulés : " +
                shipStatus.getOrDefault("intact", 0) + "/" +
                shipStatus.getOrDefault("hit", 0) + "/" +
                shipStatus.getOrDefault("sunk", 0)));

        stats.add(new JLabel("Dernier coup joué : " + targetPlayer.getLastMove()));

        int totalShots = hitStats.getOrDefault("hits", 0) + hitStats.getOrDefault("misses", 0);
        stats.add(new JLabel("Tirs dans l'eau : " + hitStats.getOrDefault("misses", 0)));
        stats.add(new JLabel("Cases touchées / Cases restantes : " +
                hitStats.getOrDefault("hits", 0) + " / " +
                (targetPlayer.getTotalShipSegments() - hitStats.getOrDefault("hits", 0))));

        return stats;
    }

    private JPanel createWeaponPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Arsenal"));
        panel.setPreferredSize(new Dimension(150, 0));

        weaponGroup = new ButtonGroup();

        JRadioButton rbMissile = new JRadioButton("Missile");
        rbMissile.setToolTipText("Tir classique (1 case)");
        rbMissile.setSelected(true);
        rbMissile.addActionListener(e -> controller.setWeaponMode("MISSILE"));
        weaponGroup.add(rbMissile);
        panel.add(rbMissile);

        panel.add(Box.createVerticalStrut(10));

        JRadioButton rbBomb = new JRadioButton("Bombe");
        rbBomb.setToolTipText("Tir de zone (Croix)");
        rbBomb.addActionListener(e -> controller.setWeaponMode("BOMB"));
        weaponGroup.add(rbBomb);
        panel.add(rbBomb);

        panel.add(Box.createVerticalStrut(10));

        JRadioButton rbSonar = new JRadioButton("Sonar");
        rbSonar.setToolTipText("Détecte sans dégâts");
        rbSonar.addActionListener(e -> controller.setWeaponMode("SONAR"));
        weaponGroup.add(rbSonar);
        panel.add(rbSonar);

        return panel;
    }

    private JPanel createDualGridPanel() {
        JPanel container = new JPanel(new GridLayout(1, 2, 20, 0));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("MA FLOTTE", SwingConstants.CENTER), BorderLayout.NORTH);
        leftPanel.add(createGridPanel(playerCells, false), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("RADAR (ATTAQUEZ ICI)", SwingConstants.CENTER), BorderLayout.NORTH);
        rightPanel.add(createGridPanel(opponentCells, true), BorderLayout.CENTER);

        container.add(leftPanel);
        container.add(rightPanel);
        return container;
    }

    private JPanel createGridPanel(JPanel[][] cellsArray, boolean isClickable) {
        JPanel grid = new JPanel(new GridLayout(gridSize + 1, gridSize + 1));

        grid.add(new JLabel(""));
        for (int i = 0; i < gridSize; i++) {
            grid.add(new JLabel(String.valueOf((char)('A' + i)), SwingConstants.CENTER));
        }

        for (int row = 0; row < gridSize; row++) {
            grid.add(new JLabel(String.valueOf(row + 1), SwingConstants.CENTER));

            for (int col = 0; col < gridSize; col++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(35, 35));
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cell.setBackground(new Color(173, 216, 230));

                if (isClickable) {
                    final int r = row;
                    final int c = col;
                    cell.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (inputEnabled) {
                                controller.handleHumanAttack(r, c);
                            }
                        }
                    });
                }

                cellsArray[row][col] = cell;
                grid.add(cell);
            }
        }
        return grid;
    }

    public void updateGrids() {
        Player human = game.getHumanPlayer();
        Player computer = game.getM_computerPlayer();

        updateSingleGrid(playerCells, human.getOwnGrid(), true);
        updateSingleGrid(opponentCells, computer.getOwnGrid(), false);

        this.turnLabel.setText("Tour : " + game.getTurnNumber());
        updateInfoPanel();

        this.repaint();
    }

    private void updateSingleGrid(JPanel[][] cellsUI, Grid gridModel, boolean showShips) {
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                Cell cell = gridModel.getCell(r, c);
                JPanel panel = cellsUI[r][c];

                Color color = new Color(173, 216, 230);

                if (cell.isHit()) {
                    if (cell.getEntity() != null) {
                        color = Color.RED;
                    } else {
                        color = Color.WHITE;
                    }
                }
                else if (showShips && cell.getEntity() != null) {
                    color = Color.DARK_GRAY;
                }

                panel.setBackground(color);
            }
        }
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setInputEnabled(boolean enabled) {
        this.inputEnabled = enabled;
        this.setCursor(enabled ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void showScreen() {
        setVisible(true);
    }

    @Override
    public void turnEnded(Player a, Map<Coordinate, String> s) {

    }

    @Override
    public void onCellUpdated(Player p, Coordinate c) {
        this.updateGrids();

        if (p.equals(game.getHumanPlayer()) && !game.isGameOver()) {
            this.setInputEnabled(true);
            this.setStatus("À vous de jouer !");
        }
    }

    @Override
    public void onShipSunk(Player defender) {
        setStatus("-> [LISTENER]: BATEAU COULÉ! par " + defender.getNickName());
    }

    @Override
    public void onGameOver(Player winner) {
        this.setInputEnabled(false);
        this.setStatus("PARTIE TERMINÉE ! Vainqueur: " + winner.getNickName());

        JOptionPane.showMessageDialog(this,
                "Victoire de " + winner.getNickName() + " !",
                "Fin de Partie",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onScanResult(Player player, List<ScanResult> results) {
        setStatus("Sonar détecté. Entités trouvées : " + results.size());
    }
}