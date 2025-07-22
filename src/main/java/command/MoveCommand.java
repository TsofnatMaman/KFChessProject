package command;

import board.Board;

public class MoveCommand implements ICommand{
    private final int[] from;
    private final int[] to;
    private final Board board;

    public MoveCommand(int[] from, int[] to, Board board){
        this.from = from;
        this.to = to;
        this.board = board;
    }

    @Override
    public void execute(){
        System.out.println("Moving from " + from + " to " + to);
        board.move(from, to);
    }
}
