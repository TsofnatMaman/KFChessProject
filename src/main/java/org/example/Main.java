package org.example;


import board.Board;
import game.Game;
import player.PlayerCursor;
import view.GameView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("KFChess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PlayerCursor pc1 = new PlayerCursor(0,0, Color.RED);
            PlayerCursor pc2 = new PlayerCursor(7,7,Color.BLUE);

            Game game = new Game(Board.loadFromCSV(8,8), pc1, pc2);
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
