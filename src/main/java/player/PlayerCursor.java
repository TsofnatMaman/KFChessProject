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
        if (pos.getR() > 0) pos.reduceOneRow();
    }

    public void moveDown() {
        if (pos.getR() < ROWS-1) pos.addOneRow();
    }

    public void moveLeft() {
        if (pos.getC() > 0) pos.reduceOneCol();
    }

    public void moveRight() {
        if (pos.getC() < COLS-1) pos.addOneCol();
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int squareWidth = panelWidth / ROWS;
        int squareHeight = panelHeight / COLS;

        int x = pos.getC() * squareWidth;
        int y = pos.getR() * squareHeight;

        Graphics2D g2d = (Graphics2D) g;  // המרה ל-Graphics2D

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));  // עכשיו אפשר להשתמש ב-setStroke
        g2d.drawRect(x, y, squareWidth, squareHeight);
    }

    public int getRow() {
        return pos.getR();
    }

    public int getCol() {
        return pos.getC();
    }

    public Position getPosition(){
        return pos;
    }
}
