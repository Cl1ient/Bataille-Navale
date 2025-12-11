package view;

import controller.GameController;
import model.Coordinate;
import model.GameListener;
import model.ScanResult;
import model.game.Game;
import model.map.Cell;
import model.map.Grid;
import model.player.Player;
import model.weapon.Weapon;

import javax.swing.*;
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
    private ButtonGroup weaponGroup;

    private boolean inputEnabled = true;

    public GameView(GameController controller, Game game) {
        this.controller = controller;
        this.game = game;
        this.game.addListener(this);
        this.gridSize = game.getHumanPlayer().getGridSize();

        this.playerCells = new JPanel[gridSize][gridSize];
        this.opponentCells = new JPanel[gridSize][gridSize];

        setTitle("Bataille Navale - En Jeu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createWeaponPanel(), BorderLayout.WEST);
        add(createDualGridPanel(), BorderLayout.CENTER);

        this.statusLabel = new JLabel("À vous de jouer ! Cliquez sur la grille de droite.", SwingConstants.CENTER);
        this.statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(this.statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        updateGrids();
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
        setStatus("-> [LISTENER]: Tour terminé.");
    }

    @Override
    public void onCellUpdated(Player p, Coordinate c) {
        System.out.println("-> [LISTENER]: Cellule mise à jour: " + c);
        this.updateGrids();
        if (p.equals(game.getHumanPlayer()) && !game.isGameOver()) {
            this.setInputEnabled(true);
            this.setStatus("À vous de jouer !");
        }
    }

    @Override
    public void onShipSunk(Player defender) {
        setStatus("-> [LISTENER]: BATEAU COULÉ! par " + defender.getNickName());
        System.out.println("-> [LISTENER]: bateeeeeeeeeeeeeeau coulé");

        controller.checkVictory();
    }

    @Override
    public void onGameOver(Player winner) {
        System.out.println("-> [LISTENER]: *** JEU TERMINÉ. Vainqueur: " + winner.getNickName() + " ***");

        this.setInputEnabled(false);
        this.setStatus("PARTIE TERMINÉE ! Vainqueur: " + winner.getNickName());

        JOptionPane.showMessageDialog(this,
                "Victoire de " + winner.getNickName() + " !",
                "Fin de Partie",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onScanResult(Player player, List<ScanResult> results) {
        System.out.println("-> [LISTENER]: Sonar détecté.");
        setStatus("Sonar détecté. Entités trouvées : " + results.size());
    }
}