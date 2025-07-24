package interfaces;

import pieces.Position;

public interface IBoard {

    public static final int[][] linesForPlayer = {{0,1},{6,7}};

    public void placePiece(IPiece piece);

    public boolean hasPiece(int row, int col);

    public IPiece getPiece(int row, int col);

    public IPiece getPiece(Position pos) ;

    public int getPlayerOf(int row);

    public void move(Position from, Position to);

    public void updateAll();

    public boolean isInBounds(int r, int c);

    public boolean isInBounds(Position p);

    public boolean isMoveLegal(Position from, Position to);

    public boolean isPathClear(Position from, Position to);

    public boolean canMoveOver(IPiece p);

    public boolean isMoving(IPiece p);

    public boolean isJumpLegal(IPiece p) ;

    public void jump(IPiece p);

    public IPlayer[] getPlayers();

    public int getROWS();

    public int getCOLS();
}
