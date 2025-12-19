package view.gameView;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class WeaponPanel extends JPanel {

    public WeaponPanel(GameController controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Arsenal"));
        setPreferredSize(new Dimension(150, 0));

        ButtonGroup group = new ButtonGroup();

        // Ajout des boutons
        addWeaponOption("Missile", "Tir classique (1 case)", "MISSILE", true, group, controller);
        add(Box.createVerticalStrut(10));
        addWeaponOption("Bombe", "Tir de zone (Croix)", "BOMB", false, group, controller);
        add(Box.createVerticalStrut(10));
        addWeaponOption("Sonar", "Détecte sans dégâts", "SONAR", false, group, controller);
    }

    private void addWeaponOption(String label, String tooltip, String mode, boolean selected, ButtonGroup group, GameController controller) {
        JRadioButton rb = new JRadioButton(label);
        rb.setToolTipText(tooltip);
        rb.setSelected(selected);
        // Connexion directe au contrôleur
        rb.addActionListener(e -> controller.setWeaponMode(mode));

        group.add(rb);
        add(rb);
    }
}