import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel scoreLabel;
    private JTextArea movesArea;

    public PlayerInfoPanel() {
        setLayout(new BorderLayout(5,5));
        setPreferredSize(new Dimension(200, 0));

        nameLabel = new JLabel("Player Name");
        scoreLabel = new JLabel("Score: 0");

        movesArea = new JTextArea(10, 15);
        movesArea.setEditable(false);
        movesArea.setLineWrap(true);
        movesArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(movesArea);

        JPanel topPanel = new JPanel(new GridLayout(2,1));
        topPanel.add(nameLabel);
        topPanel.add(scoreLabel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setPlayerName(String name) {
        nameLabel.setText(name);
    }

    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void addMove(String move) {
        movesArea.append(move + "\n");
    }

    public void clearMoves() {
        movesArea.setText("");
    }
}
