package state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class GraphicsDataTest {

    private BufferedImage[] dummyFrames;

    @BeforeEach
    void setUp() {
        // ניצור מערך קטן של BufferedImage דמה
        dummyFrames = new BufferedImage[3];
        for (int i = 0; i < dummyFrames.length; i++) {
            dummyFrames[i] = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        }
    }

    @Test
    void testInitialState() {
        GraphicsData gd = new GraphicsData(dummyFrames, 1, true);
        assertEquals(3, gd.getTotalFrames());
        assertEquals(0, gd.getCurrentNumFrame());
        assertTrue(gd.isLoop());
        assertEquals(1, gd.getFramesPerSec());
        assertNotNull(gd.getCurrentFrame());
    }

    @Test
    void testUpdateAdvancesFrame() throws InterruptedException {
        GraphicsData gd = new GraphicsData(dummyFrames, 10, true); // 10 FPS

        int initialFrame = gd.getCurrentNumFrame();
        // נחכה מעט יותר מ-1/10 שנייה כדי שהעדכון יעבור לפריים הבא
        Thread.sleep(110);

        gd.update();
        int nextFrame = gd.getCurrentNumFrame();

        assertEquals((initialFrame + 1) % gd.getTotalFrames(), nextFrame);
    }

    @Test
    void testIsAnimationFinished_LoopTrue() {
        GraphicsData gd = new GraphicsData(dummyFrames, 1, true);
        // כי לולאה - אף פעם לא מסתיימת
        assertFalse(gd.isAnimationFinished());
    }

    @Test
    void testIsAnimationFinished_LoopFalse() {
        GraphicsData gd = new GraphicsData(dummyFrames, 1, false);
        // מדמים שהגענו לפריים האחרון
        for (int i = 0; i < dummyFrames.length - 1; i++) {
            gd.update();
        }
        // force currentFrame to last frame
        while (gd.getCurrentNumFrame() < gd.getTotalFrames() - 1) {
            gd.update();
        }

        assertTrue(gd.isAnimationFinished());
    }

    @Test
    void testResetResetsFrame() {
        GraphicsData gd = new GraphicsData(dummyFrames, 1, true);
        gd.update();
        assertNotEquals(0, gd.getCurrentNumFrame());
        gd.reset("someState", new int[]{0, 0});
        assertEquals(0, gd.getCurrentNumFrame());
    }
}
