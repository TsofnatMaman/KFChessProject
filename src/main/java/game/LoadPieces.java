package game;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadPieces {

    public static final int ROWS = 8;
    public static final int COLS = 8;

    // מטריצה סטטית של חלקים
    public static final String[][] board = new String[ROWS][COLS];

    static {
        loadFromCSV();
    }

    /**
     * טוען חלקים מקובץ CSV ישירות אל תוך המטריצה הסטטית.
     */
    private static void loadFromCSV() {
        String csvResourcePath = "/board/board.csv";

        try (InputStream is = LoadPieces.class.getResourceAsStream(csvResourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < ROWS) {
                String[] cells = line.split(",");
                for (int col = 0; col < Math.min(cells.length, COLS); col++) {
                    String pieceCode = cells[col].trim();
                    if (!pieceCode.isEmpty()) {
                        board[row][col] = pieceCode;
                    } else {
                        board[row][col] = null;
                    }
                }
                row++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * מחזיר את המטריצה הסטטית של החלקים.
     *
     * @return מטריצת חלקים
     */
    public static String[][] getBoardMatrix() {
        return board;
    }
}
