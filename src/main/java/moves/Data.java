package moves;

import interfaces.IBoard;
import interfaces.IPiece;
import pieces.Position;

public class Data{

    IBoard board;
    IPiece pieceFrom;
    Position to;

    public Data(IBoard board, IPiece fromPiece, Position to){
        this.board = board;
        this.pieceFrom = fromPiece;
        this.to = to;
    }
}