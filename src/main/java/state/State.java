package state;

import java.awt.geom.Point2D;

public class State {
    private PhysicsData physics;
    private GraphicsData graphics;

    private int startRow, startCol;
    private int targetRow, targetCol;
    private long startTimeMillis;
    private int tileSize;

    public State(int startRow, int startCol, int targetRow, int targetCol,
                 int tileSize, PhysicsData physics, GraphicsData graphics) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.tileSize = tileSize;
        this.physics = physics;
        this.graphics = graphics;
        this.startTimeMillis = System.currentTimeMillis();
    }

    public int getTileSize() {
        return tileSize;
    }

    public PhysicsData getPhysics() {
        return physics;
    }

    public GraphicsData getGraphics() {
        return graphics;
    }

    public Point2D.Double getCurrentPosition() {
        double speed = physics.getSpeedMetersPerSec(); // פיקסלים לשנייה
        long now = System.currentTimeMillis();
        double elapsedSec = (now - startTimeMillis) / 1000.0;

        // מיקום התחלתי וסופי בפיקסלים
        double startX = startCol * tileSize;
        double startY = startRow * tileSize;
        double endX = targetCol * tileSize;
        double endY = targetRow * tileSize;

        double dx = endX - startX;
        double dy = endY - startY;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        if (totalDistance == 0) {
            return new Point2D.Double(startX, startY);
        }

        double distanceSoFar = speed * elapsedSec;
        double t = Math.min(1.0, distanceSoFar / totalDistance);

        double currentX = startX + dx * t;
        double currentY = startY + dy * t;

        return new Point2D.Double(currentX, currentY);
    }

    public void reset() {
        startTimeMillis = System.currentTimeMillis();
        if (graphics != null) graphics.reset();
        if (physics != null) physics.reset();
    }

    public void update() {
        if (graphics != null) graphics.update();
        physics.update();
    }
}
