package interfaces;

import pieces.Position;

import java.awt.*;

public interface IPlayerCursor {
    public void moveUp();

    public void moveDown();

    public void moveLeft();

    public void moveRight();

    public void draw(Graphics g, int panelWidth, int panelHeight) ;

    public int getRow();

    public int getCol();

    public Position getPosition();
}
