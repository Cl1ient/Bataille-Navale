package view;

import controller.GameController;
import model.Coordinate;
import model.EntityType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlacementView extends JFrame {

    private final GameController controller;
    private final int gridSize;
    private final Map<EntityType, Integer> boatsToPlace;

    private EntityType selectedEntityType;
    private boolean isHorizontal = true;
    private final Map<EntityType, List<Coordinate>> placedEntitiesMap;
    private List<Coordinate> currentPreviewCoords = new ArrayList<>();

    private JPanel gridPanel;
    private JLabel statusLabel;
    private JComboBox<EntityType> cbEntitySelector;
    private JCheckBox chkOrientation;
    private JButton btnValidate;

    private final JPanel[][] gridCells;

    public PlacementView(GameController controller, int gridSize, Map<EntityType, Integer> boatsToPlace) {
        this.controller = controller;
        this.gridSize = gridSize;
        this.boatsToPlace = new HashMap<>(boatsToPlace);
        this.placedEntitiesMap = new HashMap<>();
        this.gridCells = new JPanel[gridSize][gridSize];

        setTitle("Placement des Entités");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(createControlPanel(), BorderLayout.NORTH);

        this.gridPanel = createGridPanel();
        mainPanel.add(this.gridPanel, BorderLayout.CENTER);

        this.statusLabel = new JLabel("Statut: Prêt à placer les bateaux. Cliquez, glissez, relâchez.", SwingConstants.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(this.statusLabel, BorderLayout.SOUTH);

        updateEntitySelector();
        updateStatus();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        cbEntitySelector = new JComboBox<>();
        cbEntitySelector.addActionListener(e -> {
            selectedEntityType = (EntityType) cbEntitySelector.getSelectedItem();
            updateStatus();
            updateGridDisplay();
        });
        panel.add(new JLabel("Entité à placer :"));
        panel.add(cbEntitySelector);

        chkOrientation = new JCheckBox("Horizontal", true);
        chkOrientation.addActionListener(e -> {
            isHorizontal = chkOrientation.isSelected();
            updateGridDisplay();
        });
        panel.add(chkOrientation);

        btnValidate = new JButton("Valider le Placement et Commencer");
        btnValidate.setEnabled(false);
        btnValidate.addActionListener(e -> onValidateClicked());
        panel.add(btnValidate);

        return panel;
    }

    private JPanel createGridPanel() {
        JPanel panel = new JPanel(new GridLayout(gridSize + 1, gridSize + 1));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(new JLabel(""));
        for (int c = 0; c < gridSize; c++) {
            panel.add(new JLabel(String.valueOf((char) ('A' + c)), SwingConstants.CENTER));
        }

        for (int r = 0; r < gridSize; r++) {
            panel.add(new JLabel(String.valueOf(r + 1), SwingConstants.CENTER));
            for (int c = 0; c < gridSize; c++) {
                JPanel cell = createGridCell(r, c);
                gridCells[r][c] = cell;
                panel.add(cell);
            }
        }
        return panel;
    }

    private JPanel createGridCell(int row, int col) {
        JPanel cell = new JPanel();
        cell.setPreferredSize(new Dimension(30, 30));
        cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cell.setBackground(Color.WHITE);

        MouseAdapter placementAdapter = new MouseAdapter() {
            private Coordinate dragStartCoord = null;

            @Override
            public void mousePressed(MouseEvent e) {
                dragStartCoord = new Coordinate(row, col);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragStartCoord != null) {
                    attemptPlacement(row, col);
                }
                dragStartCoord = null;
                updateGridDisplay();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedEntityType != null) {
                    previewPlacement(row, col);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (dragStartCoord == null) {
                    updateGridDisplay();
                }
            }
        };

        cell.addMouseListener(placementAdapter);
        cell.addMouseMotionListener(placementAdapter);

        return cell;
    }

    private void previewPlacement(int startRow, int startCol) {
        if (selectedEntityType == null || boatsToPlace.getOrDefault(selectedEntityType, 0) <= 0) {
            return;
        }

        int size = getEntitySize(selectedEntityType);
        List<Coordinate> requestedCoords = calculateEntityCoordinates(startRow, startCol, size);
        boolean isValid = validatePlacement(requestedCoords);
        updateGridDisplay();
        for (Coordinate coord : requestedCoords) {
            if (coord.getX() >= 0 && coord.getX() < gridSize && coord.getY() >= 0 && coord.getY() < gridSize) {
                JPanel cell = gridCells[coord.getX()][coord.getY()];
                if (cell != null) {
                    cell.setBackground(isValid ? Color.YELLOW : Color.RED);
                }
            }
        }
        currentPreviewCoords = requestedCoords;
    }

    private void attemptPlacement(int endRow, int endCol) {
        if (selectedEntityType == null || boatsToPlace.getOrDefault(selectedEntityType, 0) <= 0) {
            return;
        }

        int size = getEntitySize(selectedEntityType);
        List<Coordinate> requestedCoords = calculateEntityCoordinates(endRow, endCol, size);
        if (!validatePlacement(requestedCoords)) {
            JOptionPane.showMessageDialog(this, "Placement invalide (hors limites, chevauchement ou sur l'île).", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        registerPlacement(requestedCoords);
        boatsToPlace.put(selectedEntityType, boatsToPlace.get(selectedEntityType) - 1);
        updateGridDisplay();
        updateStatus();
        updateEntitySelector();
    }

    private void registerPlacement(List<Coordinate> coords) {
        placedEntitiesMap.computeIfAbsent(selectedEntityType, k -> new java.util.ArrayList<>()).addAll(coords);
    }

    private List<Coordinate> calculateEntityCoordinates(int r, int c, int size) {
        List<Coordinate> coords = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            int row = isHorizontal ? r : r + i;
            int col = isHorizontal ? c + i : c;
            coords.add(new Coordinate(row, col));
        }
        return coords;
    }

    private boolean validatePlacement(List<Coordinate> coords) {
        boolean islandMode = controller.getGameConfiguration().isIslandMode();

        for (Coordinate coord : coords) {
            if (coord.getX() < 0 || coord.getX() >= gridSize || coord.getY() < 0 || coord.getY() >= gridSize) {
                return false;
            }

            if (islandMode && isBoat(selectedEntityType)) {
                if (coord.getX() >= 3 && coord.getX() <= 6 && coord.getY() >= 3 && coord.getY() <= 6) {
                    return false;
                }
            }

            if (isOccupied(coord)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOccupied(Coordinate coord) {
        return placedEntitiesMap.values().stream()
                .flatMap(List::stream)
                .anyMatch(c -> c.getX() == coord.getX() && c.getY() == coord.getY());
    }

    private void updateGridDisplay() {
        boolean islandMode = controller.getGameConfiguration().isIslandMode();

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                JPanel cell = gridCells[r][c];
                Coordinate currentCoord = new Coordinate(r, c);
                EntityType placedType = getPlacedEntityType(currentCoord);

                Color color = Color.WHITE;

                if (islandMode && r >= 3 && r <= 6 && c >= 3 && c <= 6) {
                    color = new Color(238, 214, 175);
                }

                if (placedType != null) {
                    switch (placedType) {
                        case AIRCRAFT_CARRIER:
                        case CRUISER:
                        case DESTROYER:
                        case SUBMARINE:
                        case TORPEDO:
                            color = Color.DARK_GRAY;
                            break;
                        case BLACK_HOLE:
                            color = new Color(75, 0, 130);
                            break;
                        case STORM:
                            color = new Color(100, 100, 255);
                            break;
                        default:
                            color = Color.PINK;
                            break;
                    }
                }
                cell.setBackground(color);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void updateEntitySelector() {
        cbEntitySelector.removeAllItems();
        boolean boatsRemaining = boatsToPlace.entrySet().stream()
                .anyMatch(entry -> entry.getValue() > 0 && isBoat(entry.getKey()));
        List<EntityType> availableTypes = boatsToPlace.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .filter(type -> {
                    if (boatsRemaining) {
                        return isBoat(type);
                    } else {
                        return !isBoat(type);
                    }
                })
                .collect(Collectors.toList());

        for (EntityType type : availableTypes) {
            cbEntitySelector.addItem(type);
        }

        if (!availableTypes.isEmpty()) {
            cbEntitySelector.setSelectedIndex(0);
            selectedEntityType = (EntityType) cbEntitySelector.getSelectedItem();
        } else {
            selectedEntityType = null;
        }
    }

    private void updateStatus() {
        int totalRemaining = boatsToPlace.values().stream().mapToInt(Integer::intValue).sum();
        boolean boatsRemaining = boatsToPlace.entrySet().stream()
                .anyMatch(entry -> entry.getValue() > 0 && isBoat(entry.getKey()));

        if (totalRemaining == 0) {
            statusLabel.setText("PRÊT ! Tous les navires et pièges sont placés. Validez pour jouer.");
            statusLabel.setForeground(new Color(0, 100, 0));
            btnValidate.setEnabled(true);
        } else {
            btnValidate.setEnabled(false);
            statusLabel.setForeground(Color.BLACK);
            if (boatsRemaining) {
                statusLabel.setText("PHASE 1 : Placez vos navires (Reste : " + totalRemaining + ")");
            } else {
                statusLabel.setText("PHASE 2 : Placez vos pièges (Reste : " + totalRemaining + ")");
            }
        }
    }

    private void onValidateClicked() {
        controller.startGame(placedEntitiesMap);
        this.dispose();
    }

    private int getEntitySize(EntityType type) {
        return switch (type) {
            case AIRCRAFT_CARRIER -> 5;
            case CRUISER -> 4;
            case DESTROYER, SUBMARINE -> 3;
            case TORPEDO -> 2;
            default -> 1;
        };
    }

    private EntityType getPlacedEntityType(Coordinate coord) {
        for (Map.Entry<EntityType, List<Coordinate>> entry : placedEntitiesMap.entrySet()) {
            for (Coordinate placedCoord : entry.getValue()) {
                if (placedCoord.getX() == coord.getX() && placedCoord.getY() == coord.getY()) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private boolean isBoat(EntityType type) {
        if (type == null) return false;
        return switch (type) {
            case AIRCRAFT_CARRIER, CRUISER, DESTROYER, SUBMARINE, TORPEDO -> true;
            default -> false;
        };
    }

    public void showScreen() {
        this.setVisible(true);
    }
}