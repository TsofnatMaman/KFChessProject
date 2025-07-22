package board;

import pieces.Piece;
import pieces.PiecesFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Board {
    private Piece[][] board;
    private static final int ROWS = 8;
    private static final int COLS = 8;

    public Board(){
        board = new Piece[ROWS][COLS];
    }

    public void placePiece(Piece piece){
        if(piece.getRow() >= 0 && piece.getRow() < 8 && piece.getCol() >= 0 && piece.getCol() <8)
            board[piece.getRow()][piece.getCol()] = piece;
        else
            throw new IllegalArgumentException("row or col is not valid row="+piece.getRow()+" col="+piece.getCol());
    }

    public void drawAll(Graphics g) {
        int squareWidth = g.getClipBounds().width / 8;
        int squareHeight = g.getClipBounds().height / 8;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = board[row][col];
                if (p != null) {
                    int x = col * squareWidth;
                    int y = row * squareHeight;
                    p.draw(g, x, y, squareWidth, squareHeight);
                }
            }
        }
    }


    public void updateAll() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = board[row][col];
                if (p != null) {
                    p.update();
                }
            }
        }
    }

    public static Board loadBoardFromCSV(){//String csvResourcePath) {
        String csvResourcePath = "/board/board.csv";
        Board board = new Board();

        try (InputStream is = Board.class.getResourceAsStream(csvResourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < ROWS) {
                String[] cells = line.split(",");
                for (int col = 0; col < Math.min(cells.length, COLS); col++) {
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
}
