package View;

import Controller.GameController;
import Model.Game.GameConfiguration;
import Model.Boat.BoatType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class ConfigView extends JFrame {

    //private final GameController controller;

    private JComboBox<Integer> cbGridSize;
    private JCheckBox chkIslandMode;
    private JComboBox<Integer> cbPlacementLevel;

    private Map<BoatType, JSpinner> boatSpinners;
    private static final Integer MIN_BOATS = 1;
    private static final Integer MAX_BOATS = 3;

    /**
     * Constructor for the Configuration Screen.
     * @param controller The GameController instance to handle configuration submission.
     */
    public ConfigView(GameController controller) {
        //this.controller = controller;
        this.boatSpinners = new HashMap<>();


        setTitle("Configuration de la Partie (Niveau 1 & 2)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));


        JPanel settingsPanel = createSettingsPanel();

        JButton btnNext = new JButton("Passer au Placement des Entités");
        btnNext.addActionListener(this::onNextStepClicked);

        add(settingsPanel, BorderLayout.CENTER);
        add(btnNext, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Create configuration pannel with configuration option.
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Taille de la Grille (6x6 à 10x10):"), gbc);
        cbGridSize = new JComboBox<>(new Integer[]{6, 7, 8, 9, 10});
        cbGridSize.setSelectedItem(10); // Défaut à 10x10
        gbc.gridx = 1; panel.add(cbGridSize, gbc);
        row++;


        chkIslandMode = new JCheckBox("Activer le Mode Île (Règle C5)");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; panel.add(chkIslandMode, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; panel.add(new JLabel("Placement Joueur (Niveau 1-3):"), gbc);
        cbPlacementLevel = new JComboBox<>(new Integer[]{1, 2, 3});
        cbPlacementLevel.setSelectedItem(3);
        gbc.gridx = 1; panel.add(cbPlacementLevel, gbc);
        row++;


        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JLabel("Nombre de Bateaux par Type (1 à 3):"), gbc);
        row++;
        Integer jspinner = 1;

        for (BoatType type : BoatType.values()) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(MIN_BOATS, MIN_BOATS, MAX_BOATS, jspinner));
            boatSpinners.put(type, spinner);

            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
            panel.add(new JLabel(type.name().replace('_', ' ') + ":"), gbc);
            gbc.gridx = 1; panel.add(spinner, gbc);
            row++;
        }

        return panel;
    }


    private void onNextStepClicked(ActionEvent event) {

        /*
        GameConfiguration config = createConfigurationObject();

        if (!validateConfiguration(config)) {
            JOptionPane.showMessageDialog(this,
                "Erreur: La configuration dépasse les limites du jeu.",
                "Validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        //controller.setGameConfig(config);
        //controller.callPlaceEntityView();
        */

        this.dispose();
    }

    public void showScreen() {
        this.setVisible(true);
    }
}