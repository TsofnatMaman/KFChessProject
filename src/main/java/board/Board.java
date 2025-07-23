package board;

import pieces.Moves;
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
        if (!isInBounds(row, col))
            return null;
        return board[row][col];
    }

    public Piece getPiece(int[] pos) {
        return getPiece(pos[0], pos[1]);
    }

    public int getPlayerOf(int row) {
        List<List<Integer>> rowsOfPlayer = List.of(
                List.of(0, 1), // שחקן 0
                List.of(6, 7) // שחקן 1
        );

        if (rowsOfPlayer.get(0).contains(row))
            return 0;
        else
            return 1;
    }

    public void move(int[] from, int[] to) {
        if (!isInBounds(from[0], from[1]) || !isInBounds(to[0], to[1]))
            return;

        Piece piece = board[from[0]][from[1]];
        if (piece != null) {
            piece.move(to);
        }
    }

    public void updateAll() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    // שמור את המיקום הישן לפני העדכון
                    int oldRow = piece.getRow();
                    int oldCol = piece.getCol();

                    piece.update();

                    int newRow = piece.getRow();
                    int newCol = piece.getCol();

                    if (piece.getCurrentState().isActionFinished()) {
                        // טיפול באכילה
                        int targetRow = piece.getCurrentState().getTargetRow();
                        int targetCol = piece.getCurrentState().getTargetCol();
                        Piece target = board[targetRow][targetCol];
                        if (target != null && target != piece && !target.isCaptured()) {
                            target.markCaptured();
                        }
                    }

                    // אם המיקום השתנה, נעביר במערך הלוח
                    if (oldRow != newRow || oldCol != newCol) {
                        board[oldRow][oldCol] = null;       // מנקים את המיקום הישן
                        board[newRow][newCol] = piece;      // מכניסים למיקום החדש
                    }
                }
            }
        }

        // ניקוי כלים שסומנו כ-captured
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Piece p = board[row][col];
                if (p != null && p.isCaptured()) {
                    board[row][col] = null;
                }
            }
        }
    }

    public void captureAt(int row, int col) {
        if (!isInBounds(row, col))
            return;

        Piece captured = board[row][col];
        if (captured != null) {
            System.out.println("Piece captured at " + row + "," + col);
            board[row][col] = null;
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public boolean isMoveLegal(int[] from, int[] to) {
        Piece fromPiece = getPiece(from);
        if (fromPiece == null)
            return false;

        if (fromPiece.getType().charAt(0) != 'N' && !isPathClear(from[0], from[1], to[0], to[1]))
            return false;

        Piece toPiece = getPiece(to);
        if (toPiece != null && fromPiece.getPlayer() == toPiece.getPlayer())
            return false;

        List<Moves.Move> moves = fromPiece.getMoves().getMoves();
        boolean isLegal = false;
        for (Moves.Move step : moves)
            if (from[0] + step.getDx() == to[0] && from[1] + step.getDy() == to[1]) {
                isLegal = true;
                break;
            }

        String currentState = fromPiece.getCurrentStateName();
        if (currentState.equals("short_rest") || currentState.equals("long_rest"))
            return false;

        return isLegal;
    }

    public boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
        int dRow = Integer.signum(toRow - fromRow);
        int dCol = Integer.signum(toCol - fromCol);

        int row = fromRow + dRow;
        int col = fromCol + dCol;

        while (row != toRow || col != toCol) {
            if (getPiece(row, col) != null)
                return false;
            row += dRow;
            col += dCol;
        }
        return true;
    }

    public boolean isJumpLegal(Piece p) {
        String currentState = p.getCurrentStateName();
        return !(currentState.equals("short_rest") || currentState.equals("long_rest"));
    }

    public void jump(Piece p) {
        if (p == null)
            return;
        p.jump();
    }
}
