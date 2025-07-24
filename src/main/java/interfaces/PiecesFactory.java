package interfaces;

import board.BoardConfig;
import graphics.GraphicsLoader;
import interfaces.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pieces.Piece;
import pieces.Position;
import state.GraphicsData;
import state.PhysicsData;
import state.State;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class PiecesFactory {

    private static double TILE_SIZE;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Piece createPieceByCode(String code, Position pos, BoardConfig config) {
        TILE_SIZE = config.tileSize;

        // ... המשך כפי שכבר בנינו, תוך שימוש ב־tileSize
        Map<EState, IState> states = new HashMap<>();
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
                EState nextState = EState.valueOf(physicsNode.path("next_state_when_finished").asText(stateName).toUpperCase());

                IPhysicsData physics = new PhysicsData(speed, nextState);

                JsonNode graphicsNode = root.path("graphics");
                int fps = graphicsNode.path("frames_per_sec").asInt(1);
                boolean isLoop = graphicsNode.path("is_loop").asBoolean(true);

                BufferedImage[] sprites = GraphicsLoader.loadAllSprites(code, EState.valueOf(stateName.toUpperCase()));
                if (sprites.length == 0) {
                    System.err.println("No sprites for state: " + stateName);
                    continue;
                }

                IGraphicsData graphics = new GraphicsData(sprites, fps, isLoop);
                IState state = new State(stateName, pos, pos, TILE_SIZE, physics, graphics);
                states.put(EState.valueOf(stateName.toUpperCase()), state);
            }

            if (states.isEmpty()) return null;

            // שלב 3 – צור את ה-Piece עם המצב הראשון כברירת מחדל
            EState initialState = EState.IDLE;
            return new Piece(code, states, initialState, pos);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
