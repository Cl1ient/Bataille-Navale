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

    private final Grid m_gridModel;
    private final boolean m_isIslandMode;
    private final int m_gridSize;
    private final JPanel[][] m_cellsUI;

    private final Consumer<Coordinate> onClickAction;

    private boolean inputEnabled = true;

    public GridPanel(Grid m_gridModel, boolean m_isIslandMode, Consumer<Coordinate> onClickAction) {
        this.m_gridModel = m_gridModel;
        this.m_isIslandMode = m_isIslandMode;
        this.m_gridSize = m_gridModel.getSize();
        this.onClickAction = onClickAction;
        this.m_cellsUI = new JPanel[m_gridSize][m_gridSize];

        setLayout(new GridLayout(m_gridSize + 1, m_gridSize + 1));
        initializeGrid();
    }

    private void initializeGrid() {
        add(new JLabel(""));

        for (int i = 0; i < m_gridSize; i++) {
            add(new JLabel(String.valueOf((char)('A' + i)), SwingConstants.CENTER));
        }

        for (int row = 0; row < m_gridSize; row++) {
            add(new JLabel(String.valueOf(row + 1), SwingConstants.CENTER));

            for (int col = 0; col < m_gridSize; col++) {
                JPanel cell = createCell(row, col);
                m_cellsUI[row][col] = cell;
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
                onClickAction.accept(new Coordinate(row, col));
            }
        });
        return cell;
    }

    public void updateGridDisplay(boolean showUnvisitedShips) {
        for (int r = 0; r < m_gridSize; r++) {
            for (int c = 0; c < m_gridSize; c++) {
                Cell cellModel = m_gridModel.getCell(r, c);
                m_cellsUI[r][c].setBackground(getCellColor(cellModel, r, c, showUnvisitedShips));
            }
        }
        repaint();
    }

    private Color getCellColor(Cell cell, int r, int c, boolean showShips) {
        boolean isIslandZone = (r >= 3 && r <= 6 && c >= 3 && c <= 6);

        if (m_isIslandMode && isIslandZone) {
            if (!cell.isHit()) return new Color(238, 214, 175);
            if (cell.getEntity() != null) return new Color(34, 139, 34);
            return Color.GRAY;
        }

        if (cell.isHit()) {
            if (cell.getEntity() != null) {
                return cell.getEntity().isSunk() ? new Color(128, 0, 0) : Color.RED;
            }
            return Color.WHITE;
        }
        else if (showShips && cell.getEntity() != null) {
            return Color.DARK_GRAY;
        }

        return new Color(173, 216, 230);
    }

    public void setInputEnabled(boolean enabled) {
        this.inputEnabled = enabled;
        setCursor(enabled ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
}