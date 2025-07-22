package state;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhysicsDataTest {

    @Test
    public void testGettersAndSetters() {
        PhysicsData pd = new PhysicsData(5.0, "idle");

        assertEquals(5.0, pd.getSpeedMetersPerSec());
        assertEquals("idle", pd.getNextStateWhenFinished());

        pd.setSpeedMetersPerSec(10.0);
        pd.setNextStateWhenFinished("move");

        assertEquals(10.0, pd.getSpeedMetersPerSec());
        assertEquals("move", pd.getNextStateWhenFinished());
    }

    @Test
    public void testResetDoesNotThrow() {
        PhysicsData pd = new PhysicsData(1.0, "idle");
        // רק וודא שהשיטה לא זורקת שגיאות
        pd.reset("move", new int[]{1, 2});
    }

    @Test
    public void testUpdateDoesNotThrow() {
        PhysicsData pd = new PhysicsData(1.0, "idle");
        // רק וודא שהשיטה לא זורקת שגיאות
        pd.update();
    }
}
