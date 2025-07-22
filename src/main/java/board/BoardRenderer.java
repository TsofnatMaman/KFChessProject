package board;

import pieces.Piece;
import pieces.PieceRenderer;

import java.awt.*;

public class BoardRenderer {
    public static void draw(Graphics g, Board board, int panelWidth, int panelHeight) {
        int squareWidth = panelWidth / board.ROWS;
        int squareHeight = panelHeight / board.COLS;

        for (int row = 0; row < board.ROWS; row++) {
            for (int col = 0; col < board.COLS; col++) {
                Piece p = board.getPiece(row, col);
                if (p != null) {
                    PieceRenderer.draw(g, p, squareWidth, squareHeight);
                }
            }
        }
    }
}
