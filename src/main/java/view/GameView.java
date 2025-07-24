package view;

import game.Game;
import interfaces.IPlayerCursor;
import player.PlayerCursor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameView extends JPanel {
    private final BoardPanel boardPanel;
    private final Game model;
    private Timer timer;

    public GameView(Game model) {
        this.model = model;
        setLayout(new BorderLayout());

        IPlayerCursor c1 = model.getPlayer1().getCursor();
        IPlayerCursor c2 = model.getPlayer2().getCursor();

        boardPanel = new BoardPanel(model.getBoard(), c1, c2);
        add(boardPanel, BorderLayout.CENTER);

        // קישור פעולות לשחקנים
        boardPanel.setOnPlayer1Action((v) -> model.handleSelection(model.getPlayer1()));
        boardPanel.setOnPlayer2Action((v) -> model.handleSelection(model.getPlayer2()));

        // בקשת פוקוס לקבלת קלט מהמקלדת
        SwingUtilities.invokeLater(() -> boardPanel.requestFocusInWindow());

        // בקשה לפוקוס גם בעת לחיצה בעכבר על הלוח
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardPanel.requestFocusInWindow();
            }
        });
    }

    public void run() {
        startGameLoop();
    }

    public void startGameLoop() {
        if (timer == null) {
            timer = new Timer(16, e -> {
                if (model.win() == -1) {
                    model.update();
                    boardPanel.updateAll();
                    boardPanel.repaint();
                } else {
                    stopGameLoop();
                    JOptionPane.showMessageDialog(this, "Game Over. Winner: Player " + model.win());
                }
            });
        }
        timer.start();
    }

    public void stopGameLoop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}
