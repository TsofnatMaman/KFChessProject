package view;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying player information such as name, score, and moves.
 */
public class PlayerInfoPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel scoreLabel;
    private JTextArea movesArea;

    /**
     * Constructs the player info panel and initializes UI components.
     */
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

    /**
     * Sets the player's name.
     */
    public void setPlayerName(String name) {
        nameLabel.setText(name);
    }

    /**
     * Sets the player's score.
     */
    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Adds a move to the moves area for display.
     * @param move The move to add.
     */
    public void addMove(String move) {
        movesArea.append(move + "\n");
    }

    /**
     * Clears all moves from the moves area.
     */
    public void clearMoves() {
        movesArea.setText("");
    }
}
