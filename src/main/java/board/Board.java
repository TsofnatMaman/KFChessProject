package board;

import pieces.Moves;
import pieces.Piece;
import player.Player;
import java.util.List;

public class Board {
    private final Piece[][] board;
    public final Player[] players;
    public final int ROWS = 8;
    public final int COLS = 8;
    public final double wM;
    public final double hM;

    public static final int[][] linesForPlayer = {{0,1},{6,7}};

    public Board(double wM, double hM, Player[] players) {
        this.board = new Piece[ROWS][COLS];
        this.players = players;

        for (Player p : players)
            for (Piece piece : p.getPieces()) {
                String[] pos = piece.getId().split(",");
                board[Integer.parseInt(pos[0])][Integer.parseInt(pos[1])] = piece;
            }

        this.wM = wM;
        this.hM = hM;
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
        return (linesForPlayer[0][0] <= row && row <= linesForPlayer[0][1]) ? 0 : 1;
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
                    int oldRow = piece.getRow();
                    int oldCol = piece.getCol();

                    // אכילה לפני תנועה
                    if (piece.getCurrentState().isActionFinished()) {
                        int targetRow = piece.getCurrentState().getTargetRow();
                        int targetCol = piece.getCurrentState().getTargetCol();

                        Piece target = board[targetRow][targetCol];
                        if (target != null && target != piece && !target.isCaptured() && !isMoving(target)) {
                            target.markCaptured();
                            players[target.getPlayer()].markPieceCaptured(target);
                            System.out.println("Captured before move: " + target.getId());
                        }
                    }

                    piece.update();

                    int newRow = piece.getRow();
                    int newCol = piece.getCol();

                    // אכילה אחרי הנחיתה
                    Piece someoneThere = board[newRow][newCol];
                    if (someoneThere != null && someoneThere != piece && !someoneThere.isCaptured()) {
                        someoneThere.markCaptured();
                        players[someoneThere.getPlayer()].markPieceCaptured(someoneThere);
                        System.out.println("Captured on landing: " + someoneThere.getId());
                    }

                    if (oldRow != newRow || oldCol != newCol) {
                        board[oldRow][oldCol] = null;
                        board[newRow][newCol] = piece;
                    }
                }
            }
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
            if (!canMoveOver(getPiece(row, col)))
                return false;
            row += dRow;
            col += dCol;
        }
        return true;
    }

    public boolean canMoveOver(Piece p) {
        return p == null || isMoving(p);
    }

    public boolean isMoving(Piece p) {
        if (p == null) return false;
        String name = p.getCurrentStateName();
        return name.equals("move") || name.equals("jump");
    }

    public boolean isJumpLegal(Piece p) {
        String currentState = p.getCurrentStateName();
        return !(currentState.equals("short_rest") || currentState.equals("long_rest"));
    }

    public void jump(Piece p) {
        if (p == null) return;
        p.jump();
    }
}
