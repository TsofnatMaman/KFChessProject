package interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Moves {

    public class Move{
        int dx;
        int dy;

        Move(int dx, int dy){
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }

    private final List<Move> moves;

    public List<Move> getMoves() {
        return moves;
    }

    public Moves(String pieceType) throws IOException {
        moves = new ArrayList<>();

        String resourcePath = "pieces/" + pieceType + "/moves.txt";
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
