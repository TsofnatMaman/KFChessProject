package board;

import interfaces.IBoard;
import interfaces.IPiece;
import pieces.PieceRenderer;

import java.awt.*;

public class BoardRenderer {
    public static void draw(Graphics g, IBoard board, int panelWidth, int panelHeight) {
        int squareWidth = panelWidth / board.getROWS();
        int squareHeight = panelHeight / board.getCOLS();

        for (int row = 0; row < board.getROWS(); row++) {
            for (int col = 0; col < board.getCOLS(); col++) {
                IPiece p = board.getPiece(row, col);
                if (p != null) {
                    PieceRenderer.draw(g, p, squareWidth, squareHeight);
                }
            }
        }
    }
}
