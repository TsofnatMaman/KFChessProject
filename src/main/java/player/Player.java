package player;

import board.BoardConfig;
import interfaces.*;
import game.LoadPieces;
import pieces.Position;

import java.util.ArrayList;
import java.util.List;

import static board.Board.linesForPlayer;

public class Player implements IPlayer{
    private final int id;
    private final IPlayerCursor cursor;
    private Position pending;
    private static int mone=0;

    private final List<IPiece> pieces;

    private boolean isfailed;

    public Player(IPlayerCursor pc, BoardConfig bc){
        id = mone++;
        this.cursor = pc;
        pending=null;
        isfailed = false;

        pieces = new ArrayList<>();

        for(int i:linesForPlayer[id])
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
        return isfailed;
    }

    public void markPieceCaptured(IPiece p){
        if(p.getType().charAt(0) == 'K')
            isfailed = true;
    }
}
