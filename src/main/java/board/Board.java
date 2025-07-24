package board;

import interfaces.*;
import pieces.Position;

import java.util.List;

public class Board implements IBoard {
    private final IPiece[][] board;
    public final IPlayer[] players;
    public final BoardConfig boardConfig;

    public static final int[][] linesForPlayer = {{0,1},{6,7}};

    public Board(int rows, int cols, double wM, double hM, IPlayer[] players) {
        boardConfig = new BoardConfig(rows, cols, wM, hM);
        this.board = new IPiece[rows][cols];
        this.players = players;

        for (IPlayer p : players)
            for (IPiece piece : p.getPieces()) {
                String[] pos = piece.getId().split(",");
                board[Integer.parseInt(pos[0])][Integer.parseInt(pos[1])] = piece;
            }
    }

    @Override
    public void placePiece(IPiece piece) {
        int row = piece.getRow();
        int col = piece.getCol();
        if (isInBounds(row, col)) {
            board[row][col] = piece;
        } else {
            throw new IllegalArgumentException("Invalid position row=" + row + ", col=" + col);
        }
    }

    @Override
    public boolean hasPiece(int row, int col) {
        return isInBounds(row, col) && board[row][col] != null;
    }

    @Override
    public IPiece getPiece(int row, int col) {
        if (!isInBounds(row, col))
            return null;
        return board[row][col];
    }

    @Override
    public IPiece getPiece(Position pos) {
        return getPiece(pos.getR(), pos.getC());
    }

    @Override
    public int getPlayerOf(int row) {
        return (linesForPlayer[0][0] <= row && row <= linesForPlayer[0][1]) ? 0 : 1;
    }

    @Override
    public void move(Position from, Position to) {
        if (!isInBounds(from) || !isInBounds(to))
            return;

        IPiece piece = board[from.getR()][from.getC()];
        if (piece != null) {
            piece.move(to);
        }
    }

    public void updateAll() {
        // שלב ראשון - איפוס המיקום הקודם
        for (int row = 0; row < boardConfig.numRows; row++) {
            for (int col = 0; col < boardConfig.numCols; col++) {
                IPiece piece = board[row][col];
                if (piece != null) {
                    int newRow = piece.getRow();
                    int newCol = piece.getCol();
                    if (newRow != row || newCol != col) {
                        board[row][col] = null;
                    }
                }
            }
        }

        // שלב שני - עדכון מצב ואכילה לפני תזוזה
        for (IPlayer p : players) {
            for (IPiece piece : p.getPieces()) {
                if (piece.isCaptured()) continue;

                if (piece.getCurrentState().isActionFinished()) {
                    int targetRow = piece.getCurrentState().getTargetRow();
                    int targetCol = piece.getCurrentState().getTargetCol();

                    IPiece target = board[targetRow][targetCol];
                    if (target != null && target != piece && !target.isCaptured() && !isMoving(target)) {
                        target.markCaptured();
                        players[target.getPlayer()].markPieceCaptured(target);
                        System.out.println("Captured before move: " + target.getId());
                    }
                }

                piece.update();
            }
        }

        // שלב שלישי - אכילה לאחר הנחיתה ועדכון המיקום בלוח
        for (IPlayer p : players) {
            for (IPiece piece : p.getPieces()) {
                if (piece.isCaptured()) continue;

                int row = piece.getRow();
                int col = piece.getCol();

                IPiece existing = board[row][col];
                if (existing != null && existing != piece && !existing.isCaptured()) {
                    if (isMoving(piece)) {
                        existing.markCaptured();
                        players[existing.getPlayer()].markPieceCaptured(existing);
                        System.out.println("Captured on landing: " + existing.getId());
                    } else {
                        System.out.println("No capture: piece not jumping on landing");
                    }
                }

                board[row][col] = piece;
            }
        }
    }

    @Override
    public boolean isInBounds(int r, int c) {
        return r >= 0 && r < boardConfig.numRows && c >= 0 && c < boardConfig.numCols;
    }

    public boolean isInBounds(Position p){
        return isInBounds(p.getR(), p.getC());
    }

    @Override
    public boolean isMoveLegal(Position from, Position to) {
        IPiece fromPiece = getPiece(from);
        if (fromPiece == null)
            return false;

        // Check resting states first
        EState currentState = fromPiece.getCurrentStateName();
        if (currentState == EState.SHORT_REST || currentState == EState.LONG_REST)
            return false;

        // Check if the move is in the legal move list
        List<Moves.Move> moves = fromPiece.getMoves().getMoves();
        int dx = to.getR() - from.getR();
        int dy = to.getC() - from.getC();
        boolean isLegal = moves.stream().anyMatch(m -> m.getDx() == dx && m.getDy() == dy);

        if (!isLegal)
            return false;

        // Check path clearance (except knights)
        if (fromPiece.getType().charAt(0) != 'N' && !isPathClear(from, to))
            return false;

        // Check if capturing own piece
        IPiece toPiece = getPiece(to);
        return toPiece == null || fromPiece.getPlayer() != toPiece.getPlayer();
    }


    @Override
    public boolean isPathClear(Position from, Position to) {
        int dRow = Integer.signum(to.getR() - from.getR());
        int dCol = Integer.signum(to.getC() - from.getC());

        Position current = from.add(dRow, dCol);

        while (!current.equals(to)) {
            if (!canMoveOver(getPiece(current)))
                return false;
            current = current.add(dRow, dCol);
        }

        return true;
    }


    @Override
    public boolean canMoveOver(IPiece p) {
        return p == null || isMoving(p);
    }

    @Override
    public boolean isMoving(IPiece p) {
        if (p == null) return false;
        EState name = p.getCurrentStateName();
        return EState.MOVE.equals(name) || EState.JUMP.equals(name);
    }

    @Override
    public boolean isJumpLegal(IPiece p) {
        EState currentState = p.getCurrentStateName();
        return !(currentState.equals(EState.SHORT_REST) || currentState.equals(EState.LONG_REST));
    }

    @Override
    public void jump(IPiece p) {
        if (p == null) return;
        p.jump();
    }

    @Override
    public IPlayer[] getPlayers() {
        return players;
    }

    @Override
    public int getCOLS() {
        return boardConfig.numCols;
    }

    @Override
    public int getROWS() {
        return boardConfig.numRows;
    }
}
