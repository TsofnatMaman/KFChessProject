package pieces;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphics.GraphicsLoader;
import state.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class PiecesFactory {

    private static final int TILE_SIZE = 64;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Piece createPieceByCode(String code, int row, int col) {
        Map<String, State> states = new HashMap<>();
        String basePath = "/pieces/" + code + "/states/";

        try {
            // שלב 1 – מצא את כל המצבים הקיימים בתיקיית states
            URL dirURL = PiecesFactory.class.getResource(basePath);
            if (dirURL == null || !dirURL.getProtocol().equals("file")) {
                System.err.println("Cannot load states from: " + basePath);
                return null;
            }

            File statesDir = new File(dirURL.toURI());
            File[] subdirs = statesDir.listFiles(File::isDirectory);
            if (subdirs == null) return null;

            // שלב 2 – טען כל מצב
            for (File stateFolder : subdirs) {
                String stateName = stateFolder.getName();
                String configPath = basePath + stateName + "/config.json";
                InputStream is = PiecesFactory.class.getResourceAsStream(configPath);
                if (is == null) {
                    System.err.println("Missing config for state: " + stateName);
                    continue;
                }

                JsonNode root = mapper.readTree(is);
                JsonNode physicsNode = root.path("physics");
                double speed = physicsNode.path("speed_m_per_sec").asDouble(0.0);
                String nextState = physicsNode.path("next_state_when_finished").asText(stateName);

                PhysicsData physics = new PhysicsData(speed, nextState);

                JsonNode graphicsNode = root.path("graphics");
                int fps = graphicsNode.path("frames_per_sec").asInt(1);
                boolean isLoop = graphicsNode.path("is_loop").asBoolean(true);

                BufferedImage[] sprites = GraphicsLoader.loadAllSprites(code, stateName);
                if (sprites.length == 0) {
                    System.err.println("No sprites for state: " + stateName);
                    continue;
                }

                GraphicsData graphics = new GraphicsData(sprites, fps, isLoop);
                State state = new State(stateName, row, col, row, col, TILE_SIZE, physics, graphics);
                states.put(stateName, state);

                System.out.println("טוען מצב: " + stateName);
                System.out.println("  פריימים נטענו: " + sprites.length);
                System.out.println("  frames_per_sec: " + fps);
                System.out.println("  is_loop: " + isLoop);
                System.out.println("  next_state_when_finished: " + nextState);

            }

            if (states.isEmpty()) return null;

            // שלב 3 – צור את ה-Piece עם המצב הראשון כברירת מחדל
            String initialState = "idle";
            return new Piece(code, states, initialState, row, col);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
