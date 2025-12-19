package view.gameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameLogPanel extends JPanel {

    private final JTextArea logArea;
    private final SimpleDateFormat timeFormat;

    public GameLogPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Journal de bord",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)
        ));
        setPreferredSize(new Dimension(0, 150)); // Hauteur fixe en bas

        this.timeFormat = new SimpleDateFormat("HH:mm:ss");

        this.logArea = new JTextArea();
        this.logArea.setEditable(false);
        this.logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        this.logArea.setLineWrap(true);
        this.logArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addLog(String message) {
        String time = timeFormat.format(new Date());
        logArea.append("[" + time + "] " + message + "\n");

        // Auto-scroll vers le bas
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void addLog(String message, boolean isImportant) {
        if (isImportant) {
            addLog(">>> " + message.toUpperCase() + " <<<");
        } else {
            addLog(message);
        }
    }
}