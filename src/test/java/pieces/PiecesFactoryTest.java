package pieces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PiecesFactoryTest {

    @Test
    void testCreatePieceByCode_withValidResources() {
        Piece piece = PiecesFactory.createPieceByCode("PB", 1, 1);
        assertNotNull(piece);
        assertEquals(1, piece.getRow());
        assertEquals(1, piece.getCol());
        assertNotNull(piece.getCurrentState());
        assertEquals("idle", piece.getCurrentStateName());
    }

    @Test
    void testCreatePieceByCode_withInvalidCode() {
        Piece piece = PiecesFactory.createPieceByCode("nonexistent_piece", 0, 0);
        assertNull(piece);
    }
}
