package command;

import command.JumpCommand;
import interfaces.IBoard;
import interfaces.IPiece;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class JumpCommandTest {

    @Test
    void testExecuteJumpLegal() {
        IPiece piece = mock(IPiece.class);
        IBoard board = mock(IBoard.class);

        when(board.isJumpLegal(piece)).thenReturn(true);

        JumpCommand cmd = new JumpCommand(piece, board);
        cmd.execute();

        verify(board).jump(piece);
    }

    @Test
    void testExecuteJumpIllegal() {
        IPiece piece = mock(IPiece.class);
        IBoard board = mock(IBoard.class);

        when(board.isJumpLegal(piece)).thenReturn(false);

        JumpCommand cmd = new JumpCommand(piece, board);
        cmd.execute();

        verify(board, never()).jump(piece);
    }
}
