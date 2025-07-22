package graphics;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsLoader {

    private static final Map<String, Image> cache = new HashMap<>();

    public static Image loadSprite(String pieceType, String stateName, int frameIndex) {
        String path = String.format("/pieces/%s/states/%s/sprites/%d.png", pieceType, stateName, frameIndex);

        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        try {
            Image image = ImageIO.read(GraphicsLoader.class.getResourceAsStream(path));
            cache.put(path, image);
            return image;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load sprite: " + path);
            e.printStackTrace();
            return null;
        }
    }



    public static List<Image> loadAllSprites(String code, String actionName) {
        List<Image> sprites = new ArrayList<>();
        int index = 1;

        while (true) {
            String filename = String.format("%s_%d.png", actionName, index);
            Image sprite = loadSprite(code, actionName, index);

            if (sprite == null) break;

            sprites.add(sprite);
            index++;
        }

        return sprites;
    }
}
