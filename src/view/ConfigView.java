package view;

import controller.GameController;
import model.Coordinate;
import model.EntityType;
import model.boat.BoatType;
import model.game.GameConfiguration;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigView extends JFrame {

    private final GameController controller;

    private JTextField txtNickname;
    private JComboBox<Integer> cbGridSize;
    private JCheckBox chkIslandMode;
    private JComboBox<Integer> cbPlacementLevel;

    private JLabel lblTotalCells;
    private final Map<BoatType, JSpinner> boatSpinners;

    private static final Integer MIN_BOATS = 1;
    private static final Integer MAX_BOATS = 3;
    private static final Integer MAX_TOTAL_CELLS = 35;

    private static final Map<EntityType, Integer> BOAT_SIZES = Map.of(
            EntityType.AIRCRAFT_CARRIER, 5,
            EntityType.CRUISER, 4,
            EntityType.DESTROYER, 3,
            EntityType.SUBMARINE, 3,
            EntityType.TORPEDO, 2
    );

    public ConfigView(GameController controller) {
        this.controller = controller;
        this.boatSpinners = new HashMap<>();

        setTitle("Configuration de la Partie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        JPanel settingsPanel = createSettingsPanel();

        JButton btnNext = new JButton("Passer au Placement des Entités");
        btnNext.setFont(new Font("Arial", Font.BOLD, 14));
        btnNext.addActionListener(this::onNextStepClicked);

        add(settingsPanel, BorderLayout.CENTER);
        add(btnNext, BorderLayout.SOUTH);

        updateTotalCellCount();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Pseudo du Capitaine :"), gbc);
        txtNickname = new JTextField("Capitaine", 10);
        gbc.gridx = 1;
        panel.add(txtNickname, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Taille de la Grille :"), gbc);
        cbGridSize = new JComboBox<>(new Integer[]{6, 7, 8, 9, 10});
        cbGridSize.setSelectedItem(10);
        gbc.gridx = 1;
        panel.add(cbGridSize, gbc);
        row++;

        chkIslandMode = new JCheckBox("Activer le Mode Île");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(chkIslandMode, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel("Niveau :"), gbc);
        cbPlacementLevel = new JComboBox<>(new Integer[]{1, 2, 3});
        cbPlacementLevel.setSelectedItem(2);
        gbc.gridx = 1;
        panel.add(cbPlacementLevel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JLabel("Flotte (" + MIN_BOATS + " à " + MAX_BOATS + " par type) :"), gbc);
        row++;

        ChangeListener listener = e -> updateTotalCellCount();

        for (BoatType type : BoatType.values()) {

            SpinnerNumberModel model = new SpinnerNumberModel(1, MIN_BOATS.intValue(), MAX_BOATS.intValue(), 1);
            JSpinner spinner = new JSpinner(model);

            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);

            spinner.addChangeListener(listener);
            boatSpinners.put(type, spinner);

            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
            int size = BOAT_SIZES.getOrDefault(EntityType.valueOf(type.name()), 0);
            panel.add(new JLabel(type.name().replace('_', ' ') + " (" + size + " cases) :"), gbc);

            gbc.gridx = 1;
            panel.add(spinner, gbc);
            row++;
        }

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        row++;

        lblTotalCells = new JLabel("Total cases : 0 / " + MAX_TOTAL_CELLS);
        lblTotalCells.setFont(new Font("Arial", Font.BOLD, 12));
        lblTotalCells.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(lblTotalCells, gbc);

        return panel;
    }

    private void updateTotalCellCount() {
        int total = calculateTotalShipCells();
        lblTotalCells.setText("Total cases bateaux : " + total + " / " + MAX_TOTAL_CELLS);
        if (total > MAX_TOTAL_CELLS) {
            lblTotalCells.setForeground(Color.RED);
        } else {
            lblTotalCells.setForeground(new Color(0, 100, 0));
        }
    }

    private int calculateTotalShipCells() {
        int total = 0;
        for (Map.Entry<BoatType, JSpinner> entry : boatSpinners.entrySet()) {
            BoatType bType = entry.getKey();
            Integer count = (Integer) entry.getValue().getValue();
            try {
                EntityType eType = EntityType.valueOf(bType.name());
                int size = BOAT_SIZES.getOrDefault(eType, 0);
                total += (count * size);
            } catch (Exception e) { }
        }
        return total;
    }

    private GameConfiguration createConfigurationObject() {
        String nickname = txtNickname.getText().trim();
        if (nickname.isEmpty()) nickname = "Capitaine";

        Integer gridSize = (Integer) cbGridSize.getSelectedItem();
        boolean isIslandMode = chkIslandMode.isSelected();
        Map<EntityType, List<Coordinate>> emptyPlacement = new HashMap<>();

        GameConfiguration config = new GameConfiguration(gridSize, emptyPlacement, isIslandMode, nickname);
        return config;
    }

    private Map<EntityType, Integer> getRequestedBoatCounts() {
        Map<EntityType, Integer> counts = new HashMap<>();
        for (Map.Entry<BoatType, JSpinner> entry : boatSpinners.entrySet()) {
            BoatType bType = entry.getKey();
            Integer count = (Integer) entry.getValue().getValue();
            counts.put(EntityType.valueOf(bType.name()), count);
        }
        if (!chkIslandMode.isSelected()) {
            counts.put(EntityType.STORM, 1);
            counts.put(EntityType.BLACK_HOLE, 1);
        }
        return counts;
    }

    private boolean validateConfiguration(GameConfiguration config) {
        int totalShipCells = calculateTotalShipCells();
        if (totalShipCells > MAX_TOTAL_CELLS) {
            JOptionPane.showMessageDialog(this,
                    "Total cases bateaux (" + totalShipCells + ") dépasse la limite de " + MAX_TOTAL_CELLS + " !",
                    "Règle Niveau 2", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for (Map.Entry<BoatType, JSpinner> entry : boatSpinners.entrySet()) {
            int count = (Integer) entry.getValue().getValue();
            if (count > MAX_BOATS) {
                JOptionPane.showMessageDialog(this,
                        "Maximum " + MAX_BOATS + " bateaux pour " + entry.getKey() + ".",
                        "Limite Bateaux", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        int gridSize = config.getGridSize();
        double ratio = (double) totalShipCells / (gridSize * gridSize);
        if (ratio > 0.45) {
            JOptionPane.showMessageDialog(this,
                    "Trop de bateaux pour une grille " + gridSize + "x" + gridSize + " !\noccupation : " + String.format("%.0f", ratio*100) + "%",
                    "Configuration Risquée", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void onNextStepClicked(ActionEvent event) {
        GameConfiguration config = createConfigurationObject();
        if (!validateConfiguration(config)) return;

        controller.setGameConfig(config);
        controller.setBoatsToPlace(getRequestedBoatCounts());
        controller.callPlaceEntityView();
        this.dispose();
    }

    public void showScreen() { this.setVisible(true); }
}