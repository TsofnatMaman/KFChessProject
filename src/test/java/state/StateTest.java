package state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    private PhysicsData physics;
    private GraphicsData graphics;
    private State state;
    private final int TILE_SIZE = 64;

    @BeforeEach
    public void setup() {
        // מהירות 64 פיקסלים לשנייה (1 משבצת לשנייה)
        physics = new PhysicsData(64, "idle");

        // בלי אנימציה - מצברים ריקים
        graphics = new GraphicsData(new java.awt.image.BufferedImage[1], 1, true);

        // מצב התחלתי בשורה 0, עמודה 0, יעד לשורה 1, עמודה 0
        state = new State("",0, 0, 1, 0, TILE_SIZE, physics, graphics);
    }

    @Test
    public void testInitialPosition() {
        Point2D.Double pos = state.getCurrentPosition();
        assertEquals(0, pos.getX(), 0.01);
        assertEquals(0, pos.getY(), 0.01);
    }

    @Test
    public void testUpdateMovement() throws InterruptedException {
        // אתחל למעבר משורה 0 לעמודה 1 (מרחק 64 פיקסלים)
        state.reset("move", new int[]{0, 1});

        // המתן חצי שניה (50% מהדרך)
        Thread.sleep(500);

        state.update();

        Point2D.Double pos = state.getCurrentPosition();

        // מיקום X צריך להיות בין 30 ל-34 (כמעט חצי מרחק)
        assertTrue(pos.getX() > 30 && pos.getX() < 34, "X should be halfway");
        assertEquals(0, pos.getY(), 0.01);
    }

    @Test
    public void testMovementFinishes() throws InterruptedException {
        state.reset("move", new int[]{0, 1});

        // המתן 1100 מ״ש - קצת יותר מזמן התנועה (כדי שיסיים)
        Thread.sleep(1100);

        state.update();

        // מיקום אמור להיות המדויק של המשבצת היעד
        Point2D.Double pos = state.getCurrentPosition();
        assertEquals(64, pos.getX(), 0.1);
        assertEquals(0, pos.getY(), 0.1);

        // מצב התנועה אמור להסתיים
        assertTrue(state.isActionFinished());
    }

    @Test
    public void testGetCurrentRowCol() {
        state.reset("move", new int[]{3, 4});
        state.update();

        // מאחר והתחלנו מ-0,0 ועדיין לא התקדם, השורה והעמודה צריכות להיות 0
        assertEquals(0, state.getCurrentRow());
        assertEquals(0, state.getCurrentCol());
    }
}
