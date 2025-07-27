package interfaces;

import pieces.EPieceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the legal moves for a piece type.
 */
public class Moves {

    /**
     * Represents a single move with delta x and delta y.
     */
    public class Move {
        int dx;
        int dy;

        /**
         * Constructs a move with the given delta x and delta y.
         * @param dx Delta x (row difference)
         * @param dy Delta y (column difference)
         */
        Move(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        /**
         * Gets the delta x of the move.
         * @return Delta x value
         */
        public int getDx() {
            return dx;
        }

        /**
         * Gets the delta y of the move.
         * @return Delta y value
         */
        public int getDy() {
            return dy;
        }
    }

    private final List<Move> moves;

    /**
     * Gets the list of legal moves.
     * @return List of Move objects
     */
    public List<Move> getMoves() {
        return moves;
    }

    /**
     * Loads moves for a piece type from a resource file.
     * @param pieceType The type of the piece
     * @throws IOException If resource not found or error reading
     */
    public Moves(EPieceType pieceType) throws IOException {
        moves = new ArrayList<>();

        String resourcePath = "pieces/" + pieceType.getVal() + "/moves.txt";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split(":")[0].split(",");
                    if (parts.length == 2) {
                        int dx = Integer.parseInt(parts[0]);
                        int dy = Integer.parseInt(parts[1]);
                        moves.add(new Move(dx, dy));
                    }
                }
            }
        }
    }
}
