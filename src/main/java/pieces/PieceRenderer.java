package pieces;

import interfaces.IPiece;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class PieceRenderer {
    public static void draw(Graphics g, IPiece p, int squareWidth, int squareHeight) {
        BufferedImage frame = p.getCurrentState().getGraphics().getCurrentFrame();

        double x = p.getCol();
        double y = p.getRow();

        if (p.getCurrentStateName().equals("move")) {
            Point2D.Double pos = p.getCurrentPixelPosition();
            x = pos.x / 64.0;
            y = pos.y / 64.0;
        }

        Point2D.Double pos = p.getCurrentPixelPosition();
        int pixelX = (int) (pos.x * squareWidth / 64.0);
        int pixelY = (int) (pos.y * squareHeight / 64.0);

        g.drawImage(frame, pixelX, pixelY, squareWidth, squareHeight, null);
    }

}
