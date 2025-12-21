package view.placementView;

import model.Coordinate;
import model.EntityType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PlacementGridPanel extends JPanel {

    private final PlacementView m_parentView;
    private final int m_gridSize;
    private final JPanel[][] m_cells;

    public PlacementGridPanel(PlacementView m_parentView, int m_gridSize) {
        this.m_parentView = m_parentView;
        this.m_gridSize = m_gridSize;
        this.m_cells = new JPanel[m_gridSize][m_gridSize];

        setLayout(new GridLayout(m_gridSize + 1, m_gridSize + 1));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        initGrid();
    }

    private void initGrid() {
        add(new JLabel(""));
        for (int c = 0; c < m_gridSize; c++) {
            add(new JLabel(String.valueOf((char) ('A' + c)), SwingConstants.CENTER));
        }

        for (int r = 0; r < m_gridSize; r++) {
            add(new JLabel(String.valueOf(r + 1), SwingConstants.CENTER));

            for (int c = 0; c < m_gridSize; c++) {
                JPanel cell = createCell(r, c);
                m_cells[r][c] = cell;
                add(cell);
            }
        }
    }

    private JPanel createCell(int r, int c) {
        JPanel cell = new JPanel();
        cell.setPreferredSize(new Dimension(30, 30));
        cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cell.setBackground(Color.WHITE);

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                m_parentView.onHover(r, c);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                m_parentView.refreshGrid();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                m_parentView.onClick(r, c);
            }
        };

        cell.addMouseListener(adapter);
        return cell;
    }

    public void updateDisplay(PlacementModel model, List<Coordinate> previewCoords, boolean previewValid) {
        for (int r = 0; r < m_gridSize; r++) {
            for (int c = 0; c < m_gridSize; c++) {
                JPanel cell = m_cells[r][c];
                Color color = Color.WHITE;
                if (model.isIslandMode() && r >= 3 && r <= 6 && c >= 3 && c <= 6) {
                    color = new Color(238, 214, 175);
                }
                EntityType type = model.getPlacedEntityTypeAt(r, c);
                if (type != null) {
                    color = getEntityColor(type);
                }
                if (previewCoords != null) {
                    for (Coordinate coord : previewCoords) {
                        if (coord.getX() == r && coord.getY() == c) {
                            color = previewValid ? Color.GREEN : new Color(255, 100, 100); // Vert ou Rouge pâle
                        }
                    }
                }

                cell.setBackground(color);
            }
        }
    }

    private Color getEntityColor(EntityType type) {
        return switch (type) {
            case AIRCRAFT_CARRIER, CRUISER, DESTROYER, SUBMARINE, TORPEDO -> Color.DARK_GRAY;
            case BLACK_HOLE -> new Color(75, 0, 130);
            case STORM -> new Color(100, 100, 255);
            default -> Color.PINK;
        };
    }
}