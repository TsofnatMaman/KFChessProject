package board;

import interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pieces.Position;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardTest {

    BoardConfig boardConfig;
    IPlayer player1;
    IPlayer player2;
    IPlayer[] players;

    IPiece piece1;
    IPiece piece2;

    Board board;

    @BeforeEach
    void setup() {
        boardConfig = new BoardConfig(new Dimension(8,8), new Dimension(640, 640));

        // מוקים של שחקנים
        player1 = mock(IPlayer.class);
        player2 = mock(IPlayer.class);
        players = new IPlayer[] { player1, player2 };

        // מוקים של כלים
        piece1 = mock(IPiece.class);
        piece2 = mock(IPiece.class);

        // הגדרת ID ומיקום לכלים
        when(piece1.getId()).thenReturn("1,1");
        when(piece1.getRow()).thenReturn(1);
        when(piece1.getCol()).thenReturn(1);
        when(piece1.getPlayer()).thenReturn(0);
        when(piece1.isCaptured()).thenReturn(false);
        when(piece1.getCurrentStateName()).thenReturn(EState.IDLE);
        when(piece1.getCurrentState()).thenReturn(mock(IState.class));

        when(piece2.getId()).thenReturn("2,2");
        when(piece2.getRow()).thenReturn(2);
        when(piece2.getCol()).thenReturn(2);
        when(piece2.getPlayer()).thenReturn(1);
        when(piece2.isCaptured()).thenReturn(false);
        when(piece2.getCurrentStateName()).thenReturn(EState.IDLE);
        when(piece2.getCurrentState()).thenReturn(mock(IState.class));

        // שחקנים מחזירים רשימת כלים
        when(player1.getPieces()).thenReturn(List.of(piece1));
        when(player2.getPieces()).thenReturn(List.of(piece2));

        // יצירת לוח
        board = new Board(boardConfig, players);
    }

    @Test
    void testPlacePieceAndGetPiece() {
        // שים את piece1 במיקום שלו
        board.placePiece(piece1);

        IPiece fetched = board.getPiece(1, 1);
        assertNotNull(fetched);
        assertEquals(piece1, fetched);
    }

    @Test
    void testHasPiece() {
        board.placePiece(piece1);

        assertTrue(board.hasPiece(1, 1));
        assertFalse(board.hasPiece(0, 0));
    }

    @Test
    void testIsInBounds() {
        assertTrue(board.isInBounds(0, 0));
        assertTrue(board.isInBounds(7, 7));
        assertFalse(board.isInBounds(-1, 0));
        assertFalse(board.isInBounds(8, 8));
    }

    @Test
    void testGetPlayerOfByRow() {
        // לפי הגדרות BoardConfig, שורות 0,1 שייכות לשחקן 0, שורות 6,7 לשחקן 1
        assertEquals(0, board.getPlayerOf(0));
        assertEquals(0, board.getPlayerOf(1));
        assertEquals(1, board.getPlayerOf(6));
        assertEquals(1, board.getPlayerOf(7));
    }

    @Test
    void testMoveUpdatesPiecePosition() {
        Position from = new Position(1, 1);
        Position to = new Position(2, 2);

        board.placePiece(piece1);

        board.move(from, to);

        // וידוא שהקריאה ל-piece.move(to) קרתה
        verify(piece1).move(to);
    }

    @Test
    void testIsPathClear() {
        Position from = new Position(0, 0);
        Position to = new Position(0, 3);

        // נמקם כלים על הלוח - בשורות שמניעות חסימה/null
        // במקרה הזה, הדרך פנויה - לא שמים כלים בין from ל-to
        assertTrue(board.isPathClear(from, to));
    }

    @Test
    void testIsPathClearWithBlock() {
        Position from = new Position(0, 0);
        Position to = new Position(0, 3);

        IPiece blocker = mock(IPiece.class);
        when(blocker.canMoveOver()).thenReturn(false);

        // נמקם את החוסם בלוח במיקום (0,1)
        // כדי שזה יהיה בפועל בתוך boardGrid
        when(blocker.getRow()).thenReturn(0);
        when(blocker.getCol()).thenReturn(1);
        board.placePiece(blocker);

        assertFalse(board.isPathClear(from, to));
    }

}
