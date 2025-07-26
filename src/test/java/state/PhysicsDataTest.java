package state;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import interfaces.EState;
import pieces.Position;
import state.PhysicsData;

public class PhysicsDataTest {

    private PhysicsData physicsData;
    private Position startPos;
    private Position targetPos;
    private final double tileSize = 100.0; // נניח שגודל תא הוא 100 פיקסלים

    @BeforeEach
    public void setup() {
        startPos = new Position(0, 0);
        targetPos = new Position(0, 3); // מרחק 3 עמודות
        physicsData = new PhysicsData(1.0, EState.IDLE); // 1 מטר לשנייה, מצב סיום IDLE
        physicsData.reset(EState.MOVE, startPos, targetPos, tileSize, System.nanoTime());
    }

    @Test
    public void testInitialPositionAfterReset() {
        assertEquals(0, physicsData.getCurrentX());
        assertEquals(0, physicsData.getCurrentY());
    }

    @Test
    public void testUpdateMovesPositionTowardsTarget() throws InterruptedException {
        physicsData.setSpeedMetersPerSec(300); // מהירות גבוהה מאוד כדי להבטיח תנועה מהירה
        physicsData.reset(EState.MOVE, startPos, targetPos, tileSize, System.nanoTime());

        Thread.sleep(50); // מחכים 50 מילישניות

        physicsData.update();

        double currentX = physicsData.getCurrentX();
        double currentY = physicsData.getCurrentY();

        // מכיוון שמתחילים ב (0,0) ויוצאים לכיוון העמודה 3, ציר ה-X צריך לגדול
        assertTrue(currentX > 0, "currentX should be greater than 0 after update");
        assertEquals(0, currentY, "currentY should remain 0 because row didn't change");
    }

    @Test
    public void testIsMovementFinishedReturnsFalseWhileMoving() throws InterruptedException {
        physicsData.setSpeedMetersPerSec(1);
        physicsData.reset(EState.MOVE, startPos, targetPos, tileSize, System.nanoTime());
        Thread.sleep(10);
        physicsData.update();
        assertFalse(physicsData.isMovementFinished());
    }

    @Test
    public void testIsMovementFinishedReturnsTrueAfterEnoughTime() {
        physicsData.setSpeedMetersPerSec(1_000_000); // מהירות גבוהה מאוד
        physicsData.reset(EState.MOVE, startPos, targetPos, tileSize, System.nanoTime());

        // מחכים ומבצעים עדכונים עד שהתנועה מסתיימת, מקסימום 1 שניה לולאה
        long start = System.nanoTime();
        while (!physicsData.isMovementFinished() && (System.nanoTime() - start) < 1_000_000_000L) {
            physicsData.update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(physicsData.isMovementFinished());
    }


    @Test
    public void testGettersAfterReset() {
        physicsData.reset(EState.MOVE, startPos, targetPos, tileSize, System.nanoTime());
        assertEquals(startPos.getCol() * tileSize, physicsData.getCurrentX());
        assertEquals(startPos.getRow() * tileSize, physicsData.getCurrentY());
        assertEquals(EState.IDLE, physicsData.getNextStateWhenFinished() == null ? EState.IDLE : physicsData.getNextStateWhenFinished());
    }

    @Test
    public void testSettersAndGetters() {
        physicsData.setSpeedMetersPerSec(5.5);
        assertEquals(5.5, physicsData.getSpeedMetersPerSec());

        physicsData.setNextStateWhenFinished(EState.LONG_REST);
        assertEquals(EState.LONG_REST, physicsData.getNextStateWhenFinished());
    }
}
