package player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pieces.Position;
import player.PlayerCursor;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerCursorTest {

    private PlayerCursor cursor;

    @BeforeEach
    void setUp() {
        // מתחילים במרכז הלוח (row=4, col=4), צבע שחור לדוגמה
        cursor = new PlayerCursor(new Position(4, 4), Color.BLACK);
    }

    @Test
    void testInitialPosition() {
        assertEquals(4, cursor.getRow());
        assertEquals(4, cursor.getCol());
    }

    @Test
    void testMoveUp() {
        cursor.moveUp();
        assertEquals(3, cursor.getRow());
        cursor.moveUp();
        cursor.moveUp();
        cursor.moveUp(); // להגיע ל-0
        assertEquals(0, cursor.getRow());

        cursor.moveUp(); // לא יוצא מהגבול
        assertEquals(0, cursor.getRow());
    }

    @Test
    void testMoveDown() {
        cursor.moveDown();
        assertEquals(5, cursor.getRow());
        for (int i = 0; i < 10; i++) {
            cursor.moveDown();
        }
        assertEquals(7, cursor.getRow()); // לא יעלה מעבר לגבול
    }

    @Test
    void testMoveLeft() {
        cursor.moveLeft();
        assertEquals(3, cursor.getCol());
        for (int i = 0; i < 10; i++) {
            cursor.moveLeft();
        }
        assertEquals(0, cursor.getCol()); // לא ייצא מהגבול
    }

    @Test
    void testMoveRight() {
        cursor.moveRight();
        assertEquals(5, cursor.getCol());
        for (int i = 0; i < 10; i++) {
            cursor.moveRight();
        }
        assertEquals(7, cursor.getCol()); // לא ייצא מהגבול
    }

    @Test
    void testGetPosition() {
        Position pos = cursor.getPosition();
        assertEquals(cursor.getRow(), pos.getRow());
        assertEquals(cursor.getCol(), pos.getCol());
    }
}