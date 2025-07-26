package command;

import command.MoveCommand;
import interfaces.IBoard;
import pieces.Position;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class MoveCommandTest {

    @Test
    void testExecuteLegalMove() {
        IBoard board = mock(IBoard.class);
        Position from = new Position(1, 1);
        Position to = new Position(2, 2);

        when(board.isMoveLegal(from, to)).thenReturn(true);

        MoveCommand cmd = new MoveCommand(from, to, board);
        cmd.execute();

        verify(board).move(from, to);
    }

    @Test
    void testExecuteIllegalMove() {
        IBoard board = mock(IBoard.class);
        Position from = new Position(1, 1);
        Position to = new Position(3, 3);

        when(board.isMoveLegal(from, to)).thenReturn(false);

        MoveCommand cmd = new MoveCommand(from, to, board);
        cmd.execute();

        verify(board, never()).move(from, to);
    }
}
