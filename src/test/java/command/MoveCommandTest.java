package command;

import board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pieces.Piece;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoveCommandTest {

    private Board mockBoard;
    private Piece mockPiece;

    @BeforeEach
    public void setup() {
        mockBoard = mock(Board.class);
        mockPiece = mock(Piece.class);
    }

    @Test
    public void testExecute_CallsBoardMove() {
        int[] from = {1, 2};
        int[] to = {3, 4};

        MoveCommand cmd = new MoveCommand(from, to, mockBoard);

        cmd.execute();

        verify(mockBoard).move(from, to); // לוודא שהקריאה מתבצעת
    }

    @Test
    public void testExecute_DoesNotThrow() {
        int[] from = {0, 0};
        int[] to = {1, 1};

        doNothing().when(mockBoard).move(from, to);

        MoveCommand cmd = new MoveCommand(from, to, mockBoard);

        assertDoesNotThrow(cmd::execute);
    }
}
