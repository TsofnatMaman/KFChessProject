package game;

import board.Board;
import command.ICommand;
import command.JumpCommand;
import command.MoveCommand;
import player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player1;
    private Player player2;
    private List<ICommand> commandQueue;
    private Board board; // הלוח עצמו – מחלקה נפרדת בלוגיקה

    public Game(Player player1, Player player2) {
        this.board = new Board(8,8,new Player[]{ player1, player2 });
        this.player1 = player1;
        this.player2 = player2;
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
        int[] previous = player.getPendingFrom();
        int[] selected = player.getCursor().getPosition();

        if (previous == null) {

            // בדיקה אם החייל שייך לשחקן הנוכחי
            int piecesIdR = Integer.parseInt(board.getPiece(selected[0], selected[1]).getId().charAt(0)+"");
            if (board.getPlayerOf(piecesIdR) != player.getId()) {
                return;
            }

            if (board.hasPiece(player.getCursor().getRow(), player.getCursor().getCol()))
                player.setPendingFrom(selected);
            else
                System.err.println("choose isn't piece");
        } else {
            player.setPendingFrom(null);
            if(previous[0] == selected[0] && previous[1] == selected[1])
                addCommand(new JumpCommand(getBoard().getPiece(selected[0], selected[1]), board));
            else
                addCommand(new MoveCommand(previous, selected, board));
        }
    }

    public int win(){
        if(board.players[0].isfailed())
            return 1;
        if(board.players[1].isfailed())
            return 0;
        return -1;
    }
}
