package view.placementView;

import controller.GameController;
import model.Coordinate;
import model.EntityType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PlacementView extends JFrame {

    private final GameController controller;
    private final PlacementModel model;

    // Composants UI
    private final PlacementControlPanel controlPanel;
    private final PlacementGridPanel gridPanel;
    private final JLabel statusLabel;

    public PlacementView(GameController controller, int gridSize, Map<EntityType, Integer> boatsToPlace) {
        this.controller = controller;
        this.model = new PlacementModel(controller, boatsToPlace);
        setTitle("Placement des Entités");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        this.controlPanel = new PlacementControlPanel(this);
        this.gridPanel = new PlacementGridPanel(this, gridSize);
        this.statusLabel = new JLabel("Bienvenue", SwingConstants.CENTER);
        this.statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(controlPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        updateUIState();

        pack();
        setLocationRelativeTo(null);
    }

    public void onHover(int row, int col) {
        List<Coordinate> preview = model.calculateCoordinates(row, col);
        boolean isValid = model.validatePlacement(preview);
        gridPanel.updateDisplay(model, preview, isValid);
    }

    public void onClick(int row, int col) {
        List<Coordinate> coords = model.calculateCoordinates(row, col);

        if (model.validatePlacement(coords)) {
            model.placeEntity(coords);
            updateUIState();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void onValidate() {
        controller.startGame(model.getPlacedEntities());
        this.dispose();
    }

    public void refreshGrid() {
        gridPanel.updateDisplay(model, null, true);
    }

    private void updateUIState() {
        controlPanel.updateControls(model);
        updateStatusLabel();
        refreshGrid();
    }

    private void updateStatusLabel() {
        if (model.isFinished()) {
            statusLabel.setText("TOUT EST PLACÉ ! Cliquez sur Valider pour commencer.");
            statusLabel.setForeground(new Color(0, 100, 0));
        } else {
            statusLabel.setForeground(Color.BLACK);
            EntityType current = model.getSelectedEntityType();
            if (current != null) {
                statusLabel.setText("Placez : " + current + " (Reste global : " + model.getRemainingCount() + ")");
            } else {
                statusLabel.setText("Sélectionnez une entité.");
            }
        }
    }

    public PlacementModel getModel() { return model; }

    public void showScreen() {
        this.setVisible(true);
    }
}