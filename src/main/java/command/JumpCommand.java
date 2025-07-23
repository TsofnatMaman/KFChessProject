package command;

import board.Board;
import pieces.Piece;

public class JumpCommand implements ICommand{

    private Piece p;
    private Board board;

    public JumpCommand(Piece p, Board board){
        this.p = p;
        this.board = board;
    }

    @Override
    public void execute() {
        if(!board.isJumpLegal(p))
            return;
        board.jump(p);
    }
}
