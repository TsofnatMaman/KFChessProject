package command;

import interfaces.*;
import pieces.Position;
import utils.LogUtils;

public class MoveCommand implements ICommand {
    private final Position from;
    private final Position to;
    private final IBoard board;

    /**
     * Constructs a MoveCommand for moving a piece from one position to another.
     */
    public MoveCommand(Position from, Position to, IBoard board) {
        this.from = from;
        this.to = to;
        this.board = board;
    }

    /**
     * Executes the move command, moving the piece if the move is legal.
     */
    @Override
    public void execute() {
        if (!board.isMoveLegal(from, to)) {
            System.err.println("Illegal move from " + from + " to " + to);
            LogUtils.logDebug("Illegal move from " + from + " to " + to);
            return;
        }
        System.out.println("Moving from " + from + " to " + to);
        LogUtils.logDebug("Moving from " + from + " to " + to);
        board.move(from, to);
    }
}
