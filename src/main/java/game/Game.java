package game;

import player.Player;
import player.PlayerCursor;
import view.BoardPanel;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
    private BoardPanel boardPanel;
    private Player player1;
    private Player player2;

    public Game() {
        setLayout(new BorderLayout());

        player1 = new Player(new PlayerCursor(0, 0, Color.RED));
        player2 = new Player(new PlayerCursor(7, 7, Color.BLUE));

        boardPanel = new BoardPanel(player1.getCursor(), player2.getCursor()); // טוען לוח חדש
        add(boardPanel, BorderLayout.CENTER);

        startGameLoop();
    }

    private void startGameLoop() {
        Timer timer = new Timer(16, e -> {
            boardPanel.updateAll();
            boardPanel.repaint(); // מרנדר מחדש את הפאנל
        });
        timer.start();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
