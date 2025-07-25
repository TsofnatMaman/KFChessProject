package board;

import interfaces.*;
import pieces.Position;

import java.util.List;

public class Board implements IBoard {
    private final IPiece[][] boardGrid;
    public final IPlayer[] players;
    public final BoardConfig boardConfig;

    public Board(BoardConfig bc, IPlayer[] players) {
        boardConfig = bc;
        this.boardGrid = new IPiece[bc.numRows][bc.numCols];
        this.players = players;

        for (IPlayer p : players)
            for (IPiece piece : p.getPieces()) {
                String[] pos = piece.getId().split(",");
                boardGrid[Integer.parseInt(pos[0])][Integer.parseInt(pos[1])] = piece;
            }
    }

    @Override
    public void placePiece(IPiece piece) {
        int row = piece.getRow();
        int col = piece.getCol();
        if (isInBounds(row, col)) {
            boardGrid[row][col] = piece;
        } else {
            throw new IllegalArgumentException("Invalid position row=" + row + ", col=" + col);
        }
    }

    @Override
    public boolean hasPiece(int row, int col) {
        return isInBounds(row, col) && boardGrid[row][col] != null;
    }

    @Override
    public IPiece getPiece(int row, int col) {
        if (!isInBounds(row, col))
            return null;
        return boardGrid[row][col];
    }

    @Override
    public IPiece getPiece(Position pos) {
        return getPiece(pos.getRow(), pos.getCol());
    }

    @Override
    public int getPlayerOf(int row) {
        if (boardConfig.rowsOfPlayer.get(0).contains(row))
            return 0;
        else
            return 1;
    }

    @Override
    public int getPlayerOf(Position pos){
        return getPlayerOf(pos.getRow());
    }

    @Override
    public int getPlayerOf(IPiece piece){
        return getPlayerOf(Integer.parseInt(piece.getId().split(",")[0]));
    }

    @Override
    public void move(Position from, Position to) {
        if (!isInBounds(from) || !isInBounds(to))
            return;

        IPiece piece = boardGrid[from.getRow()][from.getCol()];
        if (piece != null) {
            piece.move(to);
        }
    }

    //TODO: understand
    public void updateAll() {
        // שלב ראשון - איפוס המיקום הקודם
        for (int row = 0; row < boardConfig.numRows; row++) {
            for (int col = 0; col < boardConfig.numCols; col++) {
                IPiece piece = boardGrid[row][col];
                if (piece != null) {
                    int newRow = piece.getRow();
                    int newCol = piece.getCol();
                    if (newRow != row || newCol != col) {
                        boardGrid[row][col] = null;
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

                    IPiece target = boardGrid[targetRow][targetCol];
                    if (target != null && target != piece && !target.isCaptured() && target.canMoveOver()) {
                        if (target.getCurrentStateName() == EState.JUMP){
                            players[piece.getPlayer()].markPieceCaptured(piece);
                            System.out.println("Captured before move: " + piece.getId());
                        } else {
                            players[target.getPlayer()].markPieceCaptured(target);
                            System.out.println("Captured before move: " + target.getId());
                        }
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

                IPiece existing = boardGrid[row][col];
                if (existing != null && existing != piece && !existing.isCaptured()) {
                    System.out.println(existing.getCurrentStateName());
                    if (existing.getCurrentStateName() != EState.JUMP) {
                        players[existing.getPlayer()].markPieceCaptured(existing);
                        System.out.println("Captured on landing: " + existing.getId());
                    } else {
                        players[piece.getPlayer()].markPieceCaptured(piece);
                        System.out.println("No capture: piece not jumping on landing");
                    }
                }

                boardGrid[row][col] = piece;
            }
        }
    }

    @Override
    public boolean isInBounds(int r, int c) {
        return r >= 0 && r < boardConfig.numRows && c >= 0 && c < boardConfig.numCols;
    }

    public boolean isInBounds(Position p){
        return isInBounds(p.getRow(), p.getCol());
    }

    @Override
    public boolean isMoveLegal(Position from, Position to) {
        IPiece fromPiece = getPiece(from);
        if (fromPiece == null)
            return false;

        // Check resting states first
        if (!fromPiece.getCurrentStateName().isCanAction())
            return false;

        // Check if the move is in the legal move list
        List<Moves.Move> moves = fromPiece.getMoves().getMoves();
        int dx = to.getRow() - from.getRow();
        int dy = to.getCol() - from.getCol();
        boolean isLegal = moves.stream().anyMatch(m -> m.getDx() == dx && m.getDy() == dy);

        if (!isLegal)
            return false;

        // Check path clearance (except knights)
        if (fromPiece.getType().charAt(0) != 'N' && !isPathClear(from, to)) {
            isPathClear(from, to);
            return false;
        }

        // Check if capturing own piece
        IPiece toPiece = getPiece(to);
        return toPiece == null || fromPiece.getPlayer() != toPiece.getPlayer();
    }


    @Override
    public boolean isPathClear(Position from, Position to) {
        int dRow = Integer.signum(to.dx(from));//Integer.signum(to.getRow() - from.getRow());
        int dCol = Integer.signum(to.dy(from));//Integer.signum(to.getCol() - from.getCol());

        Position current = from.add(dRow, dCol);

        while (!current.equals(to)) {
            if (getPiece(current) != null && !getPiece(current).canMoveOver())
                return false;
            current = current.add(dRow, dCol);
        }

        return true;
    }

    @Override
    public boolean isJumpLegal(IPiece p) {
        return p.getCurrentStateName().isCanAction();
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

    @Override
    public BoardConfig getBoardConfig() {
        return boardConfig;
    }
}
