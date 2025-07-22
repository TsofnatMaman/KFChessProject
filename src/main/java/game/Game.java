package game;

import board.Board;
import command.ICommand;
import command.MoveCommand;
import player.Player;
import player.PlayerCursor;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player1;
    private Player player2;
    private List<ICommand> commandQueue;
    private Board board; // הלוח עצמו – מחלקה נפרדת בלוגיקה

    public Game(Board board, PlayerCursor cursor1, PlayerCursor cursor2) {
        this.board = board;
        player1 = new Player(cursor1);
        player2 = new Player(cursor2);
        commandQueue = new ArrayList<>();
    }

    public void addCommand(ICommand cmd){
        commandQueue.add(cmd);
    }

    public void update() {
        while (!commandQueue.isEmpty()) {
            commandQueue.remove(0).execute();
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Board getBoard() {
        return board;
    }

    public void handleSelection(Player player) {
        int[] selected = player.getCursor().getPosition();
        int[] previous = player.getPendingFrom();

        if (previous == null) {
            if (board.hasPiece(player.getCursor().getRow(), player.getCursor().getCol()))
                player.setPendingFrom(selected);
            else
                System.err.println("choose isn't piece");
        } else {
            player.setPendingFrom(null);
            addCommand(new MoveCommand(previous, selected, board));
        }
    }
}
