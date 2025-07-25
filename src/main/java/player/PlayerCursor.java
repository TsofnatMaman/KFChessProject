package player;

import interfaces.IPlayerCursor;
import pieces.Position;

import java.awt.*;

public class PlayerCursor implements IPlayerCursor {
    private Position pos;
    private final Color color;
    public final int ROWS;
    public final int COLS;

    public PlayerCursor(Position pos, Color color) {
        ROWS=8;
        COLS=8;
        this.pos = pos;
        this.color = color;
    }

    public void moveUp() {
        if (pos.getRow() > 0) pos.reduceOneRow();
    }

    public void moveDown() {
        if (pos.getRow() < ROWS-1) pos.addOneRow();
    }

    public void moveLeft() {
        if (pos.getCol() > 0) pos.reduceOneCol();
    }

    public void moveRight() {
        if (pos.getCol() < COLS-1) pos.addOneCol();
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int squareWidth = panelWidth / ROWS;
        int squareHeight = panelHeight / COLS;

        int x = pos.getCol() * squareWidth;
        int y = pos.getRow() * squareHeight;

        Graphics2D g2d = (Graphics2D) g;  // המרה ל-Graphics2D

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));  // עכשיו אפשר להשתמש ב-setStroke
        g2d.drawRect(x, y, squareWidth, squareHeight);
    }

    public int getRow() {
        return pos.getRow();
    }

    public int getCol() {
        return pos.getCol();
    }

    public Position getPosition(){
        return pos;
    }
}
