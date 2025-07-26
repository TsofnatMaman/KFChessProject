package interfaces;

import board.BoardConfig;
import pieces.Position;

public interface IBoard {

    public void placePiece(IPiece piece);

    public boolean hasPiece(int row, int col);

    public IPiece getPiece(int row, int col);

    public IPiece getPiece(Position pos) ;

    public int getPlayerOf(int row);

    public int getPlayerOf(Position pos);

    public int getPlayerOf(IPiece piece);

    public void move(Position from, Position to);

    public void updateAll();

    public boolean isInBounds(int r, int c);

    public boolean isInBounds(Position p);

    public boolean isMoveLegal(Position from, Position to);

    public boolean isPathClear(Position from, Position to);

    public boolean isJumpLegal(IPiece p) ;

    public void jump(IPiece p);

    public IPlayer[] getPlayers();

    public int getROWS();

    public int getCOLS();

    public BoardConfig getBoardConfig();
}
