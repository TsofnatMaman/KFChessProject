package pieces;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphics.GraphicsLoader;
import state.*;

import java.awt.Image;
import java.io.InputStream;
import java.util.*;

public class PiecesFactory {

    private static final int TILE_SIZE = 64;
    private static final ObjectMapper mapper = new ObjectMapper();


    public static Piece createPieceByCode(String code, int row, int col) {
        String path = "/pieces/" + code + "/states/idle/config.json";
        try (InputStream is = PiecesFactory.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Config file not found for: " + path);
                return null;
            }

            JsonNode root = mapper.readTree(is);

            JsonNode physicsNode = root.path("physics");
            double speed = physicsNode.path("speed_m_per_sec").asDouble(0.0);
            String nextState = physicsNode.path("next_state_when_finished").asText("idle");

            PhysicsData physics = new PhysicsData(speed, nextState);

            JsonNode graphicsNode = root.path("graphics");
            int fps = graphicsNode.path("frames_per_sec").asInt(1);
            boolean isLoop = graphicsNode.path("is_loop").asBoolean(true);

            List<Image> sprites = GraphicsLoader.loadAllSprites(code, "idle");
            if (sprites.isEmpty()) return null;

            GraphicsData graphics = new GraphicsData(sprites, fps, isLoop);

            State idleState = new State(row, col, row, col, TILE_SIZE, physics, graphics);

            Map<String, State> states = new HashMap<>();
            states.put("idle", idleState);

            return new Piece(states, "idle", row, col);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
