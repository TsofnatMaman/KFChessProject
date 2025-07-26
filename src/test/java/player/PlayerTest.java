package player;

import board.Dimension;
import interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pieces.Position;
import board.BoardConfig;
import player.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private IPlayerCursor cursor;
    private BoardConfig config;

    @BeforeEach
    void setup() {
        cursor = mock(IPlayerCursor.class);
        config = new BoardConfig(new Dimension(8), new Dimension(8*64)); // height, width, tile size
    }

    @Test
    void testInitialPlayerPiecesNotEmpty() {
        Player p = new Player(cursor, config);
        List<IPiece> pieces = p.getPieces();
        assertNotNull(pieces);
        assertFalse(pieces.isEmpty());
    }

    @Test
    void testSetAndGetPendingFrom() {
        Player p = new Player(cursor, config);
        Position pos = new Position(1, 1);
        p.setPendingFrom(pos);
        assertEquals(pos.getRow(), p.getPendingFrom().getRow());
        assertEquals(pos.getCol(), p.getPendingFrom().getCol());
    }

    @Test
    void testMarkKingCaptured() {
        Player p = new Player(cursor, config);
        IPiece piece = mock(IPiece.class);
        when(piece.getType()).thenReturn("King");
        p.markPieceCaptured(piece);
        assertTrue(p.isFailed());
    }
}
