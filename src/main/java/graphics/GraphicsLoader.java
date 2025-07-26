package graphics;

import interfaces.EState;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import utils.LogUtils;

public class GraphicsLoader {

    private static final Map<String, Image> cache = new HashMap<>();

    /**
     * Loads a single sprite image by piece type, state, and frame index (1-based).
     */
    public static BufferedImage loadSprite(String pieceType, EState stateName, int frameIndex) {
        String path = String.format("/pieces/%s/states/%s/sprites/%d.png", pieceType, stateName, frameIndex);

        if (cache.containsKey(path)) {
            return (BufferedImage) cache.get(path);
        }

        try {
            BufferedImage image = ImageIO.read(GraphicsLoader.class.getResourceAsStream(path));
            cache.put(path, image);
            return image;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load sprite: " + path);
            LogUtils.logDebug("Failed to load sprite: " + path);
            return null;
        }
    }

    /**
     * Loads all sprite frames in sequence (1,2,3,...) until the next file does not exist.
     */
    public static BufferedImage[] loadAllSprites(String pieceType, EState stateName) {
        List<BufferedImage> sprites = new ArrayList<>();
        int index = 1;

        while (true) {
            BufferedImage sprite = loadSprite(pieceType, stateName, index);
            if (sprite == null) break;
            sprites.add(sprite);
            index++;
        }

        return sprites.toArray(new BufferedImage[0]);
    }
}