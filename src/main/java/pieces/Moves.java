package pieces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Moves {
    private final List<int[]> moves;

    public List<int[]> getMoves() {
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
                        moves.add(new int[]{dx, dy});
                    }
                }
            }
        }
    }

}
