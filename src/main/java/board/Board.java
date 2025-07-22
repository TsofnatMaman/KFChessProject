package board;

import pieces.Piece;
import pieces.PiecesFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Board {
    private final Piece[][] board;
    public final int ROWS = 8;
    public final int COLS = 8;
    public final double wM;
    public final double hM;

    public Board(double wM, double hM) {
        this.board = new Piece[ROWS][COLS];
        this.wM = wM;
        this.hM = hM;
    }

    public static Board loadFromCSV(double wM, double hM) {
        String csvResourcePath = "/board/board.csv";
        Board board = new Board(wM, hM);

        try (InputStream is = Board.class.getResourceAsStream(csvResourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < board.ROWS) {
                String[] cells = line.split(",");
                for (int col = 0; col < Math.min(cells.length, board.COLS); col++) {
                    String pieceCode = cells[col].trim();
                    if (!pieceCode.isEmpty()) {
                        Piece piece = PiecesFactory.createPieceByCode(pieceCode, row, col);
                        if (piece != null) {
                            board.placePiece(piece);
                        }
                    }
                }
                row++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return board;
    }

    public void placePiece(Piece piece) {
        int row = piece.getRow();
        int col = piece.getCol();
        if (isInBounds(row, col)) {
            board[row][col] = piece;
        } else {
            throw new IllegalArgumentException("Invalid position row=" + row + ", col=" + col);
        }
    }

    public boolean hasPiece(int row, int col) {
        return isInBounds(row, col) && board[row][col] != null;
    }

    public Piece getPiece(int row, int col) {
        if (!isInBounds(row, col)) return null;
        return board[row][col];
    }

    public void move(int[] from, int[] to) {
        if (!isInBounds(from[0], from[1]) || !isInBounds(to[0], to[1])) return;

        Piece piece = board[from[0]][from[1]];
        if (piece != null) {
            piece.move(to);
            board[from[0]][from[1]] = null;
            board[to[0]][to[1]] = piece;
        }
    }

    public void updateAll() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    piece.update();
                    int newRow = piece.getRow();
                    int newCol = piece.getCol();
                    if ((newRow != row || newCol != col) && isInBounds(newRow, newCol)) {
                        board[row][col] = null;
                        board[newRow][newCol] = piece;
                    }
                }
            }
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }
}
