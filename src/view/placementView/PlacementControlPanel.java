package view.placementView;

import model.EntityType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlacementControlPanel extends JPanel {

    private final PlacementView m_parentView;
    private final JComboBox<EntityType> m_cbEntitySelector;
    private final JCheckBox m_chkOrientation;
    private final JButton m_btnValidate;
    private final JButton m_btnRandom;
    private final JLabel m_lblInstruction;

    public PlacementControlPanel(PlacementView parentView) {
        this.m_parentView = parentView;
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));

        m_lblInstruction = new JLabel("Entité :");
        add(m_lblInstruction);

        m_cbEntitySelector = new JComboBox<>();
        m_cbEntitySelector.addActionListener(e -> {
            EntityType selected = (EntityType) m_cbEntitySelector.getSelectedItem();
            if (selected != null) {
                parentView.getModel().setSelectedEntityType(selected);
                parentView.refreshGrid();
            }
        });
        add(m_cbEntitySelector);


        m_chkOrientation = new JCheckBox("Horizontal", true);
        m_chkOrientation.addActionListener(e -> {
            parentView.getModel().setHorizontal(m_chkOrientation.isSelected());
            parentView.refreshGrid();
        });
        add(m_chkOrientation);

        m_btnRandom = new JButton("Aléatoire");
        m_btnRandom.setToolTipText("Placer automatiquement le reste des entités");
        m_btnRandom.addActionListener(e -> parentView.onRandomPlacement());
        add(m_btnRandom);

        m_btnValidate = new JButton("Valider et Jouer");
        m_btnValidate.setEnabled(false);
        m_btnValidate.addActionListener(e -> parentView.onValidate());
        add(m_btnValidate);
    }


    public void updateControls(PlacementModel model) {
        m_cbEntitySelector.removeAllItems();
        List<EntityType> available = model.getAvailableTypes();
        for (EntityType type : available) {
            m_cbEntitySelector.addItem(type);
        }
        if (model.getSelectedEntityType() != null) {
            m_cbEntitySelector.setSelectedItem(model.getSelectedEntityType());
        }

        boolean finished = model.isFinished();
        m_btnValidate.setEnabled(finished);
        m_btnRandom.setEnabled(!finished);
        if (finished) {
            m_lblInstruction.setText("Terminé !");
            m_cbEntitySelector.setEnabled(false);
            m_chkOrientation.setEnabled(false);
        } else {
            m_lblInstruction.setText("Entité :");
            m_cbEntitySelector.setEnabled(true);
            m_chkOrientation.setEnabled(true);
        }
    }
}