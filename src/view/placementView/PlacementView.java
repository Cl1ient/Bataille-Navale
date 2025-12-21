package view.placementView;

import controller.GameController;
import model.Coordinate;
import model.EntityType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PlacementView extends JFrame {

    private final GameController m_controller;
    private final PlacementModel m_model;

    // Composants UI
    private final PlacementControlPanel m_controlPanel;
    private final PlacementGridPanel m_gridPanel;
    private final JLabel m_statusLabel;

    public PlacementView(GameController controller, int gridSize, Map<EntityType, Integer> boatsToPlace) {
        this.m_controller = controller;
        this.m_model = new PlacementModel(controller, boatsToPlace);
        setTitle("Placement des Entités");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        this.m_controlPanel = new PlacementControlPanel(this);
        this.m_gridPanel = new PlacementGridPanel(this, gridSize);
        this.m_statusLabel = new JLabel("Bienvenue", SwingConstants.CENTER);
        this.m_statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(m_controlPanel, BorderLayout.NORTH);
        add(m_gridPanel, BorderLayout.CENTER);
        add(m_statusLabel, BorderLayout.SOUTH);
        updateUIState();

        pack();
        setLocationRelativeTo(null);
    }

    public void onHover(int row, int col) {
        List<Coordinate> preview = m_model.calculateCoordinates(row, col);
        boolean isValid = m_model.validatePlacement(preview);
        m_gridPanel.updateDisplay(m_model, preview, isValid);
    }

    public void onClick(int row, int col) {
        List<Coordinate> coords = m_model.calculateCoordinates(row, col);

        if (m_model.validatePlacement(coords)) {
            m_model.placeEntity(coords);
            updateUIState();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void onValidate() {
        m_controller.startGame(m_model.getPlacedEntities());
        this.dispose();
    }

    public void refreshGrid() {
        m_gridPanel.updateDisplay(m_model, null, true);
    }

    private void updateUIState() {
        m_controlPanel.updateControls(m_model);
        updateStatusLabel();
        refreshGrid();
    }

    private void updateStatusLabel() {
        if (m_model.isFinished()) {
            m_statusLabel.setText("TOUT EST PLACÉ ! Cliquez sur Valider pour commencer.");
            m_statusLabel.setForeground(new Color(0, 100, 0));
        } else {
            m_statusLabel.setForeground(Color.BLACK);
            EntityType current = m_model.getSelectedEntityType();
            if (current != null) {
                m_statusLabel.setText("Placez : " + current + " (Reste global : " + m_model.getRemainingCount() + ")");
            } else {
                m_statusLabel.setText("Sélectionnez une entité.");
            }
        }
    }

    public PlacementModel getModel() { return m_model; }

    public void onRandomPlacement() {
        m_model.placeAllRandomly();
        updateUIState();
    }

    public void showScreen() {
        this.setVisible(true);
    }
}