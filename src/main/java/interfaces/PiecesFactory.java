package interfaces;

import board.BoardConfig;
import graphics.GraphicsLoader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pieces.Piece;
import pieces.Position;
import state.GraphicsData;
import state.PhysicsData;
import state.State;
import utils.LogUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Factory for creating pieces by code and position.
 */
public class PiecesFactory {

    private static double TILE_SIZE;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates a piece by its code, position, and board configuration.
     * Loads all states and graphics for the piece from resources.
     * @param code Piece code
     * @param pos Position on the board
     * @param config Board configuration
     * @return Piece instance or null if failed
     */
    public static Piece createPieceByCode(String code, Position pos, BoardConfig config) {
        TILE_SIZE = config.tileSize;

        // ...continue as previously built, using tileSize
        Map<EState, IState> states = new HashMap<>();
        String basePath = "/pieces/" + code + "/states/";

        try {
            // Step 1 – Find all existing states in the states directory
            URL dirURL = PiecesFactory.class.getResource(basePath);
            if (dirURL == null || !dirURL.getProtocol().equals("file")) {
                System.err.println("Cannot load states from: " + basePath);
                LogUtils.logDebug("Cannot load states from: " + basePath);
                return null;
            }

            File statesDir = new File(dirURL.toURI());
            File[] subdirs = statesDir.listFiles(File::isDirectory);
            if (subdirs == null) return null;

            // Step 2 – Load each state
            for (File stateFolder : subdirs) {
                String stateName = stateFolder.getName();
                String configPath = basePath + stateName + "/config.json";
                InputStream is = PiecesFactory.class.getResourceAsStream(configPath);
                if (is == null) {
                    System.err.println("Missing config for state: " + stateName);
                    LogUtils.logDebug("Missing config for state: " + stateName);
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
                    LogUtils.logDebug("No sprites for state: " + stateName);
                    continue;
                }

                IGraphicsData graphics = new GraphicsData(sprites, fps, isLoop);
                IState state = new State(stateName, pos, pos, TILE_SIZE, physics, graphics);
                states.put(EState.valueOf(stateName.toUpperCase()), state);
            }

            if (states.isEmpty()) {
                LogUtils.logDebug("No states loaded for piece: " + code);
                return null;
            }

            // Step 3 – Create the Piece with the first state as default
            EState initialState = EState.IDLE;
            return new Piece(code, states, initialState, pos);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.logDebug("Exception in createPieceByCode: " + e.getMessage());
            return null;
        }
    }
}
