package pieces;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

// מחלקת PieceRenderer.java (בתוך חבילה view/pieces)
public class PieceRenderer {
    public static void draw(Graphics g, Piece p, int squareWidth, int squareHeight) {
        BufferedImage frame = p.getCurrentState().getGraphics().getCurrentFrame();

        double x = p.getCol();
        double y = p.getRow();

        if (p.getCurrentStateName().equals("move")) {
            Point2D.Double pos = p.getCurrentPixelPosition();
            x = pos.x / 64.0;
            y = pos.y / 64.0;
        }

        int pixelX = (int) (x * squareWidth);
        int pixelY = (int) (y * squareHeight);

        g.drawImage(frame, pixelX, pixelY, squareWidth, squareHeight, null);
    }

}
