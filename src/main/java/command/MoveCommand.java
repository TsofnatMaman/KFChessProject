package command;

import board.Board;
import java.util.Arrays;

public class MoveCommand implements ICommand {
    private final int[] from;
    private final int[] to;
    private final Board board;

    public MoveCommand(int[] from, int[] to, Board board) {
        this.from = from;
        this.to = to;
        this.board = board;
    }

    @Override
    public void execute() {
        if (!board.isMoveLegal(from, to)) {
            boolean b= board.isMoveLegal(from, to);
            System.err.println("Illegal move from " + Arrays.toString(from) + " to " + Arrays.toString(to));
            return;
        }

        System.out.println("Moving from " + Arrays.toString(from) + " to " + Arrays.toString(to));
        board.move(from, to);
    }
}
