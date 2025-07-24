package player;

import board.Board;
import game.LoadPieces;
import pieces.Piece;
import pieces.PiecesFactory;

import java.util.ArrayList;
import java.util.List;

import static board.Board.linesForPlayer;

public class Player {
    private final int id;
    private final PlayerCursor cursor;
    private int[] pending;
    private static int mone=0;

    private final List<Piece> pieces;

    private boolean isfailed;

    public Player(PlayerCursor pc){
        id = mone++;
        this.cursor = pc;
        pending=null;
        isfailed = false;

        pieces = new ArrayList<>();

        for(int i:linesForPlayer[id])
            for(int j=0; j<8; j++)
                this.pieces.add(PiecesFactory.createPieceByCode(LoadPieces.board[i][j],i,j));

    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public int getId() {
        return id;
    }

    public PlayerCursor getCursor() {
        return cursor;
    }

    public int[] getPendingFrom() {
        return pending;
    }

    public void setPendingFrom(int[] pending) {
        this.pending = pending;
    }
    public boolean isfailed(){
        return isfailed;
    }

    public void markPieceCaptured(Piece p){
        if(p.getType().charAt(0) == 'K')
            isfailed = true;
    }
}
