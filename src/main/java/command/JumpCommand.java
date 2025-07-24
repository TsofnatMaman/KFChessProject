package command;

import interfaces.*;

public class JumpCommand implements ICommand {

    private IPiece p;
    private IBoard board;

    public JumpCommand(IPiece p, IBoard board){
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
