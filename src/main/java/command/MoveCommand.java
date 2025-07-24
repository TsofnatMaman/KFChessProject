package command;

import interfaces.*;
import pieces.Position;

import java.awt.*;
import java.util.Arrays;

public class MoveCommand implements ICommand {
    private final Position from;
    private final Position to;
    private final IBoard board;

    public MoveCommand(Position from, Position to, IBoard board) {
        this.from = from;
        this.to = to;
        this.board = board;
    }

    @Override
    public void execute() {
        if (!board.isMoveLegal(from, to)) {
            boolean b= board.isMoveLegal(from, to);
            System.err.println("Illegal move from " + from + " to " + to);
            return;
        }

        System.out.println("Moving from " + from + " to " + to);
        board.move(from, to);
    }
}
