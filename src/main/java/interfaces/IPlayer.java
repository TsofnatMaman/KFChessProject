package interfaces;

import pieces.Position;

import java.util.List;

public interface IPlayer {
    public List<IPiece> getPieces();

    public int getId();

    public IPlayerCursor getCursor();

    public Position getPendingFrom();

    public void setPendingFrom(Position pending);

    public boolean isFailed();

    public void markPieceCaptured(IPiece p);

    public ICommand handleSelection(IBoard board);
}
