package player;

import board.BoardConfig;
import command.JumpCommand;
import command.MoveCommand;
import interfaces.*;
import game.LoadPieces;
import pieces.Position;

import java.util.ArrayList;
import java.util.List;


public class Player implements IPlayer{
    private final int id;
    private final IPlayerCursor cursor;
    private Position pending;
    private static int mone=0;

    private final List<IPiece> pieces;
    public final List<List<Integer>> linesForPlayer = List.of(
            List.of(0, 1), // שחקן 0
            List.of(6, 7)  // שחקן 1
    );
    private boolean isFailed;

    public Player(IPlayerCursor pc, BoardConfig bc){
        id = mone++;
        this.cursor = pc;
        pending=null;
        isFailed = false;

        pieces = new ArrayList<>();

        for(int i:linesForPlayer.get(id))
            for(int j=0; j<8; j++)
                this.pieces.add(PiecesFactory.createPieceByCode(LoadPieces.board[i][j],new Position(i, j), bc));

    }

    public List<IPiece> getPieces() {
        return pieces;
    }

    public int getId() {
        return id;
    }

    public IPlayerCursor getCursor() {
        return cursor;
    }

    public Position getPendingFrom() {
        return pending;
    }

    public void setPendingFrom(Position pending) {
        this.pending = pending == null? null : pending.copy();
    }

    public boolean isFailed(){
        return isFailed;
    }

    public void markPieceCaptured(IPiece p){
        p.markCaptured();
        if(p.getType().charAt(0) == 'K')
            isFailed = true;
    }

    public ICommand handleSelection(IBoard board){
        Position previous = getPendingFrom();
        Position selected = getCursor().getPosition();

        if (previous == null) {
            if(board.getPlayerOf(board.getPiece(selected)) != id)
                return null;

            if (board.hasPiece(getCursor().getRow(), getCursor().getCol()) && board.getPiece(getCursor().getPosition()).getCurrentStateName().isCanAction())
                setPendingFrom(selected);
            else
                System.err.println("can not choose piece");
        } else {
            setPendingFrom(null);
            if(previous.equals(selected))
                return new JumpCommand(board.getPiece(selected), board);
            return new MoveCommand(previous, selected.copy(), board);
        }

        return null;
    }
}
