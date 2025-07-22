package player;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerCursorTest {

    @Test
    void testInitialPosition() {
        PlayerCursor cursor = new PlayerCursor(3, 5, Color.RED);
        assertEquals(3, cursor.getRow());
        assertEquals(5, cursor.getCol());
    }

    @Test
    void testMovement() {
        PlayerCursor cursor = new PlayerCursor(4, 4, Color.RED);
        cursor.moveUp();
        assertEquals(3, cursor.getRow());
        cursor.moveDown();
        assertEquals(4, cursor.getRow());
        cursor.moveLeft();
        assertEquals(3, cursor.getCol());
        cursor.moveRight();
        assertEquals(4, cursor.getCol());
    }

    @Test
    void testMoveUpAtTopEdge() {
        PlayerCursor cursor = new PlayerCursor(0, 0, Color.RED);
        cursor.moveUp();
        assertEquals(0, cursor.getRow(), "Cursor should not move above 0");
    }

}
