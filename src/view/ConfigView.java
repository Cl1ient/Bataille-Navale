package view;

import controller.GameController;
import model.Coordinate;
import model.EntityType;
import model.boat.BoatType;
import model.game.GameConfiguration;

import javax.swing.*;
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

    private final Map<BoatType, JSpinner> boatSpinners;
    private static final Integer MIN_BOATS = 1;
    private static final Integer MAX_BOATS = 3;

    /**
     * Constructor for the Configuration Screen.
     * @param controller The GameController instance to handle configuration submission.
     */
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
        panel.add(new JLabel("Taille de la Grille (6x6 à 10x10):"), gbc);

        cbGridSize = new JComboBox<>(new Integer[]{6, 7, 8, 9, 10});
        cbGridSize.setSelectedItem(10);
        gbc.gridx = 1;
        panel.add(cbGridSize, gbc);
        row++;

        chkIslandMode = new JCheckBox("Activer le Mode Île (Règle C5)");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(chkIslandMode, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel("Placement Joueur (Niveau 1-3):"), gbc);

        cbPlacementLevel = new JComboBox<>(new Integer[]{1, 2, 3});
        cbPlacementLevel.setSelectedItem(3);
        gbc.gridx = 1;
        panel.add(cbPlacementLevel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JLabel("Nombre de Bateaux par Type (1 à 3):"), gbc);
        row++;
        Integer spin = 1;
        for (BoatType type : BoatType.values()) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(MIN_BOATS, MIN_BOATS, MAX_BOATS, spin));
            boatSpinners.put(type, spinner);

            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
            panel.add(new JLabel(type.name().replace('_', ' ') + ":"), gbc);
            gbc.gridx = 1; panel.add(spinner, gbc);
            row++;
        }

        return panel;
    }


    private GameConfiguration createConfigurationObject() {
        String nickname = txtNickname.getText().trim();
        if (nickname.isEmpty()) nickname = "Joueur";

        Integer gridSize = (Integer) cbGridSize.getSelectedItem();
        boolean isIslandMode = chkIslandMode.isSelected();

        Map<EntityType, List<Coordinate>> emptyPlacement = new HashMap<>();

        return new GameConfiguration(gridSize, emptyPlacement, isIslandMode, nickname);
    }


    private Map<EntityType, Integer> getRequestedBoatCounts() {
        Map<EntityType, Integer> counts = new HashMap<>();

        for (Map.Entry<BoatType, JSpinner> entry : boatSpinners.entrySet()) {
            BoatType bType = entry.getKey();
            Integer count = (Integer) entry.getValue().getValue();

            try {
                EntityType eType = EntityType.valueOf(bType.name());
                counts.put(eType, count);
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur de mapping BoatType -> EntityType pour : " + bType);
            }
        }

        if (!chkIslandMode.isSelected()) {
            counts.put(EntityType.STORM, 1);
            counts.put(EntityType.BLACK_HOLE, 1);
        }

        return counts;
    }

    private boolean validateConfiguration(GameConfiguration config) {
        int gridSize = config.getGridSize();
        int totalCells = gridSize * gridSize;
        int usedCells = 0;

        Map<EntityType, Integer> counts = getRequestedBoatCounts();
        Map<EntityType, Integer> boatSizes = Map.of(
                EntityType.AIRCRAFT_CARRIER, 5,
                EntityType.CRUISER, 4,
                EntityType.DESTROYER, 3,
                EntityType.SUBMARINE, 3,
                EntityType.TORPEDO, 2
        );

        for (Map.Entry<EntityType, Integer> entry : counts.entrySet()) {
            int size = boatSizes.getOrDefault(entry.getKey(), 1);
            usedCells += (entry.getValue() * size);
        }

        double ratio = (double) usedCells / totalCells;
        if (ratio > 0.45) {
            JOptionPane.showMessageDialog(this,
                    "Trop d'entités pour une grille de taille " + gridSize + "x" + gridSize + " !\n" +
                            "Occupation estimée : " + String.format("%.0f", ratio * 100) + "% (Max conseillé 45%).",
                    "Configuration Invalide",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void onNextStepClicked(ActionEvent event) {
        GameConfiguration config = createConfigurationObject();

        if (!validateConfiguration(config)) {
            return;
        }
        controller.setGameConfig(config);
        controller.setBoatsToPlace(getRequestedBoatCounts());
        controller.callPlaceEntityView();

        this.dispose();
    }

    public void showScreen() {
        this.setVisible(true);
    }
}