package state;

import java.awt.*;
import java.util.List;

public class GraphicsData {
    private List<Image> sprites;
    private int framesPerSecond;
    private boolean isLoop;

    private int currentFrame = 0;
    private long lastFrameTime = 0; // נמדד בננו־שניות

    public GraphicsData(List<Image> sprites, int framesPerSecond, boolean isLoop) {
        this.sprites = sprites;
        this.framesPerSecond = framesPerSecond;
        this.isLoop = isLoop;
        reset();
    }

    public void reset() {
        currentFrame = 0;
        lastFrameTime = System.nanoTime(); // חשוב! nanoTime ולא currentTimeMillis
    }

    // מעדכן את הפריים בהתאם לזמן שעבר
    public void update() {
        if (sprites == null || sprites.isEmpty()) {
            return;
        }

        long now = System.nanoTime();
        long frameDuration = 1_000_000_000L / framesPerSecond; // ננו־שניות לפריים

        if (now - lastFrameTime > frameDuration) {
            currentFrame++;

            if (currentFrame >= sprites.size()) {
                if (isLoop) {
                    currentFrame = 0;
                } else {
                    currentFrame = sprites.size() - 1;
                }
            }

            lastFrameTime = now;
        }
    }

    public Image getCurrentFrame() {
        if (sprites == null || sprites.isEmpty()) {
            return null;
        }
        return sprites.get(currentFrame);
    }
}
