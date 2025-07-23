package board;

import pieces.Piece;
import pieces.PiecesFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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

    public int getPlayerOf(int row){
        List<List<Integer>> rowsOfPlayer = List.of(
                List.of(0, 1), // שחקן 0
                List.of(6, 7)  // שחקן 1
        );

        if(rowsOfPlayer.get(0).contains(row))
            return 0;
        else return 1;
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

    public boolean isMoveLegal(int[]from, int[]to){
        if(getPiece(to[0],to[1]) != null && getPlayerOf(from[0]) == getPlayerOf(to[0]))
            return false;

        Piece fromPiec = getPiece(from[0], from[1]);
        List<int[]> moves= fromPiec.getMoves().getMoves();

        boolean isLegal = false;

        for(int[] step : moves)
            if(from[0]+step[0] == to[0] && from[1]+step[1] == to[1]){
                isLegal = true;
                break;
            }

        if(getPiece(from[0], from[1]).getCurrentStateName().contains("rest"))
            return false;
        return isLegal;

    }

    public boolean isJumpLegal(Piece p){
        if(p.getCurrentStateName().contains("rest"))
            return false;
        return true;
    }

    public void jump(Piece p){
        if (p == null) return;
        p.jump();
    }

}
