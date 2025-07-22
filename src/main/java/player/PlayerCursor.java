package player;

import java.awt.*;

public class PlayerCursor {
    private int row;
    private int col;
    private final Color color;

    public PlayerCursor(int startRow, int startCol, Color color) {
        this.row = startRow;
        this.col = startCol;
        this.color = color;
    }

    public void moveUp() {
        if (row > 0) row--;
    }

    public void moveDown() {
        if (row < 7) row++;
    }

    public void moveLeft() {
        if (col > 0) col--;
    }

    public void moveRight() {
        if (col < 7) col++;
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int squareWidth = panelWidth / 8;
        int squareHeight = panelHeight / 8;

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
