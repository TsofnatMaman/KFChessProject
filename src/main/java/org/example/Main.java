package org.example;


import board.Board;
import game.Game;
import player.Player;
import player.PlayerCursor;
import view.GameView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("KFChess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Player p1 = new Player(new PlayerCursor(0,0, Color.RED));
            Player p2 = new Player(new PlayerCursor(7,7,Color.BLUE));

            Game game = new Game(p1, p2);
            GameView gameView = new GameView(game);
            
            // Add debug prints
            System.out.println("Debug: Initial game state setup");
            
            gameView.run();

            frame.setContentPane(gameView); // מכניס את המשחק
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
