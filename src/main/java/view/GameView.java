package view;

import game.Game;
import player.PlayerCursor;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private BoardPanel boardPanel;
    private Game model;

    public GameView(Game model) {
        this.model = model;
        setLayout(new BorderLayout());

        PlayerCursor c1 = model.getPlayer1().getCursor();
        PlayerCursor c2 = model.getPlayer2().getCursor();

        boardPanel = new BoardPanel(model.getBoard(), c1, c2);
        add(boardPanel, BorderLayout.CENTER);

        boardPanel.setOnPlayer1Action((v) -> model.handleSelection(model.getPlayer1()));
        boardPanel.setOnPlayer2Action((v) -> model.handleSelection(model.getPlayer2()));

        SwingUtilities.invokeLater(() -> boardPanel.requestFocusInWindow());

//        startGameLoop();
    }


    public void run() {//        startGameLoop();
        Timer timer = new Timer(16, e -> {
            model.update();            // עדכון הלוגיקה
            boardPanel.updateAll();    // עדכון גרפיקה (תנועה/אנימציה)
            boardPanel.repaint();      // ציור
        });
        timer.start();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
