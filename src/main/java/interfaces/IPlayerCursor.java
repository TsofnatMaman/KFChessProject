package interfaces;

import pieces.Position;

import java.awt.*;

/**
 * Interface for player cursor operations.
 */
public interface IPlayerCursor {
    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void draw(Graphics g, int panelWidth, int panelHeight);

    int getRow();

    int getCol();

    Position getPosition();
}
