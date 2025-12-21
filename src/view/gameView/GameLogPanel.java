package view.gameView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameLogPanel extends JPanel {

    private final JTextArea m_logArea;
    private final SimpleDateFormat m_timeFormat;

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

        this.m_timeFormat = new SimpleDateFormat("HH:mm:ss");

        this.m_logArea = new JTextArea();
        this.m_logArea.setEditable(false);
        this.m_logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        this.m_logArea.setLineWrap(true);
        this.m_logArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(m_logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addLog(String message) {
        String time = m_timeFormat.format(new Date());
        m_logArea.append("[" + time + "] " + message + "\n");

        // Auto-scroll vers le bas
        m_logArea.setCaretPosition(m_logArea.getDocument().getLength());
    }

    public void addLog(String message, boolean isImportant) {
        if (isImportant) {
            addLog(">>> " + message.toUpperCase() + " <<<");
        } else {
            addLog(message);
        }
    }
}