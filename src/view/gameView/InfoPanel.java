package view.gameView;

import model.game.Game;
import model.player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class InfoPanel extends JPanel {

    private final Game game;
    private final Component parentFrame;

    public InfoPanel(Game game, Component parentFrame) {
        this.game = game;
        this.parentFrame = parentFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Statistiques"));
        setPreferredSize(new Dimension(300, 0));
    }

    public void updateStats() {
        removeAll();
        Player human = game.getHumanPlayer();
        Player computer = game.getM_computerPlayer();

        add(new JLabel("<html><h3>Tour " + game.getTurnNumber() + "</h3></html>"));
        add(Box.createVerticalStrut(10));

        add(new JLabel("<html><b>--- Capitaine " + human.getNickName() + " (Vous) ---</b></html>"));
        add(createPlayerStats(human, computer));
        add(Box.createVerticalStrut(10));

        add(new JLabel("<html><b>--- Adversaire (IA) ---</b></html>"));
        add(createPlayerStats(computer, human));

        add(Box.createVerticalStrut(20));

        JButton historyBtn = new JButton("Voir l'historique complet");
        historyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        historyBtn.addActionListener(e -> showHistory());
        add(historyBtn);

        revalidate();
        repaint();
    }

    private JPanel createPlayerStats(Player target, Player opponent) {
        JPanel stats = new JPanel(new GridLayout(0, 1));
        stats.setBorder(new EmptyBorder(5, 10, 5, 10));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);

        Map<String, Integer> shipStatus = target.getShipStatusStats();
        Map<String, Integer> hitStats = opponent.getHitAccuracyStats(target);

        stats.add(new JLabel("Navires (Intact/Touché/Coulé) : " +
                shipStatus.getOrDefault("intact", 0) + "/" +
                shipStatus.getOrDefault("hit", 0) + "/" +
                shipStatus.getOrDefault("sunk", 0)));

        stats.add(new JLabel("Dernier coup : " + target.getLastMove()));
        stats.add(new JLabel("Précision (T/R) : " +
                hitStats.getOrDefault("hits", 0) + " / " +
                (target.getTotalShipSegments() - hitStats.getOrDefault("hits", 0))));

        if (target == game.getHumanPlayer()) {
            stats.add(new JLabel("<html><b>Munitions :</b></html>"));
            stats.add(new JLabel(" - Bombe : " + target.getWeaponUsesLeft("BOMB")));
            stats.add(new JLabel(" - Sonar : " + target.getWeaponUsesLeft("SONAR")));
        }

        return stats;
    }

    private void showHistory() {
        JTextArea textArea = new JTextArea(game.getHistory());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(parentFrame, scrollPane, "Historique", JOptionPane.INFORMATION_MESSAGE);
    }
}