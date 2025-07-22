package game;

import command.ICommand;
import command.MoveCommand;
import player.Player;
import player.PlayerCursor;
import view.BoardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JPanel {
    private BoardPanel boardPanel;
    private Player player1;
    private Player player2;
    private java.util.List<ICommand> commandQueue;

    public Game() {
        setLayout(new BorderLayout());

        player1 = new Player(new PlayerCursor(0, 0, Color.RED));
        player2 = new Player(new PlayerCursor(7, 7, Color.BLUE));

        boardPanel = new BoardPanel(this, player1.getCursor(), player2.getCursor()); // טוען לוח חדש
        add(boardPanel, BorderLayout.CENTER);

        commandQueue = new ArrayList<>();

        startGameLoop();
    }

    public void addCommand(ICommand cmd){
        commandQueue.add(cmd);
    }

    private void startGameLoop() {
        Timer timer = new Timer(16, e -> {
            // בצע את כל הפקודות מהתור
            while (!commandQueue.isEmpty()) {
                ICommand cmd = commandQueue.remove(0);
                cmd.execute();
            }

            boardPanel.updateAll();
            boardPanel.repaint();
        });

        timer.start();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void handleSelection(Player player) {
        int[] selected = player.getCursor().getPosition();
        int[] previous = player.getPendingFrom();

        if (previous == null) {
             if (boardPanel.getBoard().hasPiece(player.getCursor().getRow(), player.getCursor().getCol()))
                player.setPendingFrom(selected);
             else
                 System.err.println("choose isnt piece");
        } else {
            player.setPendingFrom(null);  // אפס את הבחירה

            ICommand moveCmd = new MoveCommand(previous, selected, boardPanel.getBoard());
            addCommand(moveCmd);
        }
    }

}
