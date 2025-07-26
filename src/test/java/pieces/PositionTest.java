// src/test/java/pieces/PositionTest.java
package pieces;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    void testConstructorAndGetters() {
        Position p = new Position(2, 5);
        assertEquals(2, p.getRow(), "getRow() should return the row set in the constructor");
        assertEquals(5, p.getCol(), "getCol() should return the column set in the constructor");
    }

    @Test
    void testAddCreatesNewPosition() {
        Position p1 = new Position(3, 4);
        Position p2 = p1.add(1, -2);
        assertEquals(4, p2.getRow());
        assertEquals(2, p2.getCol());
        // original unchanged
        assertEquals(3, p1.getRow());
        assertEquals(4, p1.getCol());
    }

    @Test
    void testDxDy() {
        Position p1 = new Position(5, 7);
        Position p2 = new Position(2, 10);
        assertEquals(3, p1.dx(p2), "dx = this.row - other.row");
        assertEquals(-3, p1.dy(p2), "dy = this.col - other.col");
    }

    @Test
    void testReduceAndAddOneRowCol() {
        Position p = new Position(4, 4);
        p.reduceOneRow();
        assertEquals(3, p.getRow(), "reduceOneRow() should decrement the row by 1");
        p.addOneRow();
        assertEquals(4, p.getRow(), "addOneRow() should increment the row by 1");
        p.reduceOneCol();
        assertEquals(3, p.getCol(), "reduceOneCol() should decrement the column by 1");
        p.addOneCol();
        assertEquals(4, p.getCol(), "addOneCol() should increment the column by 1");
    }

    @Test
    void testCopyCreatesEqualButDistinctInstance() {
        Position original = new Position(1, 2);
        Position copy = original.copy();
        assertEquals(original, copy, "copy() should produce an equal Position");
        assertNotSame(original, copy, "copy() should produce a distinct instance");
    }

    @Test
    void testEqualsAndToString() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 0);
        Position p3 = new Position(1, 1);
        assertTrue(p1.equals(p2), "Positions with same row/col should be equal");
        assertFalse(p1.equals(p3), "Positions with different row/col should not be equal");
        assertFalse(p1.equals(null), "Position.equals(null) should be false");
        assertFalse(p1.equals(new Object()), "Position.equals(other type) should be false");
        assertEquals("row: 0, col: 0", p1.toString(), "toString() should match the specified format");
    }
}
