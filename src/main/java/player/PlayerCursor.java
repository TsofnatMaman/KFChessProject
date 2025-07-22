package player;

import java.awt.*;

public class PlayerCursor {
    private int row;
    private int col;
    private final Color color;
    public final int ROWS;
    public final int COLS;

    public PlayerCursor(int startRow, int startCol, Color color) {
        ROWS=8;
        COLS=8;
        this.row = startRow;
        this.col = startCol;
        this.color = color;
    }

    public void moveUp() {
        if (row > 0) row--;
    }

    public void moveDown() {
        if (row < ROWS-1) row++;
    }

    public void moveLeft() {
        if (col > 0) col--;
    }

    public void moveRight() {
        if (col < COLS-1) col++;
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int squareWidth = panelWidth / ROWS;
        int squareHeight = panelHeight / COLS;

        int x = col * squareWidth;
        int y = row * squareHeight;

        Graphics2D g2d = (Graphics2D) g;  // המרה ל-Graphics2D

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));  // עכשיו אפשר להשתמש ב-setStroke
        g2d.drawRect(x, y, squareWidth, squareHeight);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int[] getPosition(){
        return new int[]{getRow(), getCol()};
    }
}
