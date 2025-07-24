package org.example;


import board.Board;
import board.BoardConfig;
import game.Game;
import interfaces.IPlayer;
import pieces.Position;
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

            BoardConfig borderConfig = new BoardConfig(8,8,64*8,64*8);

            IPlayer p1 = new Player(new PlayerCursor(new Position(0,0), Color.RED), borderConfig);
            IPlayer p2 = new Player(new PlayerCursor(new Position(7,7),Color.BLUE), borderConfig);

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
