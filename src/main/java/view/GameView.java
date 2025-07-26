package view;

import interfaces.IGame;
import interfaces.IPlayerCursor;
import utils.LogUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main panel for displaying the game and handling game loop.
 * <p>
 * Event flow:
 * <ul>
 *   <li>User interacts with the board (keyboard/mouse)</li>
 *   <li>Mouse listener requests focus for keyboard input</li>
 *   <li>Player action triggers handleSelection</li>
 *   <li>handleSelection calls addCommand</li>
 *   <li>Game loop executes commands and updates board</li>
 * </ul>
 */
public class GameView extends JPanel {
    private final BoardPanel boardPanel;
    private final IGame model;
    private Timer timer;

    /**
     * Constructs the game view with the given game model.
     * Sets up event listeners and initializes the board panel.
     * Logs initial game state setup.
     *
     * @param model The game model
     */
    public GameView(IGame model) {
        this.model = model;
        setLayout(new BorderLayout());

        IPlayerCursor c1 = model.getPlayer1().getCursor();
        IPlayerCursor c2 = model.getPlayer2().getCursor();

        boardPanel = new BoardPanel(model.getBoard(), c1, c2);
        add(boardPanel, BorderLayout.CENTER);

        // Link player actions to handleSelection and addCommand
        boardPanel.setOnPlayer1Action((v) -> model.handleSelection(model.getPlayer1()));
        boardPanel.setOnPlayer2Action((v) -> model.handleSelection(model.getPlayer2()));

        // Request focus for keyboard input
        SwingUtilities.invokeLater(() -> boardPanel.requestFocusInWindow());

        // Mouse listener: requests focus for keyboard input when clicking the board
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardPanel.requestFocusInWindow();
            }
        });

        LogUtils.logDebug("Initial game state setup");
    }

    /**
     * Starts the game loop and begins updating the game state.
     * The loop updates the model, board, and checks for win condition.
     */
    public void run() {
        startGameLoop();
    }

    /**
     * Starts the timer for the game loop, updating the board and checking for win condition.
     * If the game ends, stops the loop and shows a dialog.
     */
    public void startGameLoop() {
        if (timer == null) {
            timer = new Timer(16, e -> {
                if (model.win() == -1) {
                    model.update();
                    boardPanel.updateAll();
                    boardPanel.repaint();
                } else {
                    stopGameLoop();
                    LogUtils.logDebug("Game Over. Winner: Player " + model.win());
                    JOptionPane.showMessageDialog(this, "Game Over. Winner: Player " + model.win());
                }
            });
        }
        timer.start();
    }

    /**
     * Stops the game loop timer.
     */
    public void stopGameLoop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}
