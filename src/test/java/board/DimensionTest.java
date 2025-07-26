package board;

import board.Dimension;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DimensionTest {

    @Test
    public void testConstructorWithTwoParameters() {
        Dimension dim = new Dimension(5, 10);
        assertEquals(5, dim.getX());
        assertEquals(10, dim.getY());
    }

    @Test
    public void testConstructorWithOneParameter() {
        Dimension dim = new Dimension(7);
        assertEquals(7, dim.getX());
        assertEquals(7, dim.getY());
    }
}
