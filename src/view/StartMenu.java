package view;

import controller.GameController; // Assurez-vous d'avoir cette dépendance
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame {

    private final GameController m_controller;
    private final JButton m_btnStartConfiguration;
    private final JButton m_btnExit;

    /**
     * Constructor for the StartMenu .
     * @param controller The GameController instance to handle user actions.
     */
    public StartMenu(GameController controller) {
        this.m_controller = controller;

        setTitle("Bataille Navale - Start Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centre de l'écran

        // Titre
        JLabel lblTitle = new JLabel("⚔️ Bataille Navale ⚔️");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        m_btnStartConfiguration = new JButton("Démarrer la partie (Configuration)");
        m_btnExit = new JButton("Quitter");

        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Marge interne
        m_btnStartConfiguration.setAlignmentX(Component.CENTER_ALIGNMENT);
        m_btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(m_btnStartConfiguration);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espace vertical
        buttonPanel.add(m_btnExit);

        add(lblTitle, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH); // Marge du bas

        m_btnStartConfiguration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Notifie le Controller pour passer à l'écran de configuration
               // controller.callConfigView();
                dispose();
            }
        });

        m_btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    public void showScreen() {
        this.setVisible(true);
    }
}