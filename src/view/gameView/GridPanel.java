package view.gameView;

import model.Coordinate;
import model.map.Cell;
import model.map.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class GridPanel extends JPanel {

    private final Grid gridModel;
    private final boolean isIslandMode;
    private final int gridSize;
    private final JPanel[][] cellsUI;

    // Action à exécuter lors d'un clic (définie par GameView)
    private final Consumer<Coordinate> onClickAction;

    private boolean inputEnabled = true;

    public GridPanel(Grid gridModel, boolean isIslandMode, Consumer<Coordinate> onClickAction) {
        this.gridModel = gridModel;
        this.isIslandMode = isIslandMode;
        this.gridSize = gridModel.getSize();
        this.onClickAction = onClickAction;
        this.cellsUI = new JPanel[gridSize][gridSize];

        setLayout(new GridLayout(gridSize + 1, gridSize + 1));
        initializeGrid();
    }

    private void initializeGrid() {
        // Coin vide
        add(new JLabel(""));

        // Lettres colonnes
        for (int i = 0; i < gridSize; i++) {
            add(new JLabel(String.valueOf((char)('A' + i)), SwingConstants.CENTER));
        }

        for (int row = 0; row < gridSize; row++) {
            // Chiffres lignes
            add(new JLabel(String.valueOf(row + 1), SwingConstants.CENTER));

            for (int col = 0; col < gridSize; col++) {
                JPanel cell = createCell(row, col);
                cellsUI[row][col] = cell;
                add(cell);
            }
        }
    }

    private JPanel createCell(int row, int col) {
        JPanel cell = new JPanel();
        cell.setPreferredSize(new Dimension(35, 35));
        cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!inputEnabled) return;
                // On délègue l'action à la méthode fournie
                onClickAction.accept(new Coordinate(row, col));
            }
        });
        return cell;
    }

    public void updateGridDisplay(boolean showUnvisitedShips) {
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                Cell cellModel = gridModel.getCell(r, c);
                cellsUI[r][c].setBackground(getCellColor(cellModel, r, c, showUnvisitedShips));
            }
        }
        repaint();
    }

    private Color getCellColor(Cell cell, int r, int c, boolean showShips) {
        // 1. Gestion Mode Île
        boolean isIslandZone = (r >= 3 && r <= 6 && c >= 3 && c <= 6);

        if (isIslandMode && isIslandZone) {
            if (!cell.isHit()) return new Color(238, 214, 175); // Sable
            if (cell.getEntity() != null) return new Color(34, 139, 34); // Trésor
            return Color.GRAY; // Fouillé vide
        }

        // 2. Gestion Standard
        if (cell.isHit()) {
            if (cell.getEntity() != null) {
                return cell.getEntity().isSunk() ? new Color(128, 0, 0) : Color.RED;
            }
            return Color.WHITE; // Eau ratée
        }
        else if (showShips && cell.getEntity() != null) {
            return Color.DARK_GRAY; // Bateau visible (allié)
        }

        return new Color(173, 216, 230); // Eau par défaut
    }

    public void setInputEnabled(boolean enabled) {
        this.inputEnabled = enabled;
        setCursor(enabled ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
}