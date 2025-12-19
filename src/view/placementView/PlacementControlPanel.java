package view.placementView;

import model.EntityType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlacementControlPanel extends JPanel {

    private final PlacementView parentView;
    private final JComboBox<EntityType> cbEntitySelector;
    private final JCheckBox chkOrientation;
    private final JButton btnValidate;
    private final JLabel lblInstruction;

    public PlacementControlPanel(PlacementView parentView) {
        this.parentView = parentView;
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));

        lblInstruction = new JLabel("Entité :");
        add(lblInstruction);

        cbEntitySelector = new JComboBox<>();
        cbEntitySelector.addActionListener(e -> {
            EntityType selected = (EntityType) cbEntitySelector.getSelectedItem();
            if (selected != null) {
                parentView.getModel().setSelectedEntityType(selected);
                parentView.refreshGrid();
            }
        });
        add(cbEntitySelector);

        chkOrientation = new JCheckBox("Horizontal", true);
        chkOrientation.addActionListener(e -> {
            parentView.getModel().setHorizontal(chkOrientation.isSelected());
            parentView.refreshGrid();
        });
        add(chkOrientation);

        btnValidate = new JButton("Valider et Jouer");
        btnValidate.setEnabled(false);
        btnValidate.addActionListener(e -> parentView.onValidate());
        add(btnValidate);
    }

    public void updateControls(PlacementModel model) {
        cbEntitySelector.removeAllItems();
        List<EntityType> available = model.getAvailableTypes();
        for (EntityType type : available) {
            cbEntitySelector.addItem(type);
        }

        if (model.getSelectedEntityType() != null) {
            cbEntitySelector.setSelectedItem(model.getSelectedEntityType());
        }
        boolean finished = model.isFinished();
        btnValidate.setEnabled(finished);

        if (finished) {
            lblInstruction.setText("Terminé !");
            cbEntitySelector.setEnabled(false);
            chkOrientation.setEnabled(false);
        } else {
            lblInstruction.setText("Entité :");
            cbEntitySelector.setEnabled(true);
            chkOrientation.setEnabled(true);
        }
    }
}