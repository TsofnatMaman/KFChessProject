package board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void testLoadFromCSV() {
        Board board = Board.loadFromCSV(8, 8);
        assertNotNull(board);
        assertEquals(8, board.ROWS);
        assertEquals(8, board.COLS);
    }
}
