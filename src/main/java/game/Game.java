package game;

import board.Board;
import interfaces.ICommand;
import command.JumpCommand;
import command.MoveCommand;
import interfaces.*;
import pieces.Position;

import java.util.ArrayList;
import java.util.List;

public class Game implements IGame {
    private final IPlayer player1;
    private final IPlayer player2;
    private List<ICommand> commandQueue;
    private final IBoard board; // הלוח עצמו – מחלקה נפרדת בלוגיקה

    public Game(IPlayer player1, IPlayer player2) {
        this.board = new Board(8,8,8,8,new IPlayer[]{ player1, player2 });
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

    public IPlayer getPlayer1() {
        return player1;
    }

    public IPlayer getPlayer2() {
        return player2;
    }

    public IBoard getBoard() {
        return board;
    }

    public void handleSelection(IPlayer player) {
        Position previous = player.getPendingFrom();
        Position selected = player.getCursor().getPosition();

        if (previous == null) {

            // בדיקה אם החייל שייך לשחקן הנוכחי
            int piecesIdR = Integer.parseInt(board.getPiece(selected).getId().charAt(0)+"");
            if (board.getPlayerOf(piecesIdR) != player.getId()) {
                return;
            }

            if (board.hasPiece(player.getCursor().getRow(), player.getCursor().getCol()))
                player.setPendingFrom(selected);
            else
                System.err.println("choose isn't piece");
        } else {
            player.setPendingFrom(null);
            if(previous.equals(selected))
                addCommand(new JumpCommand(getBoard().getPiece(selected), board));
            else
                addCommand(new MoveCommand(previous, selected.copy(), board));
        }
    }

    public int win(){
        if(board.getPlayers()[0].isFailed())
            return 1;
        if(board.getPlayers()[1].isFailed())
            return 0;
        return -1;
    }
}
