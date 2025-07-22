package state;

import java.awt.*;
import java.awt.geom.Point2D;

public class State {
    private PhysicsData physics;
    private GraphicsData graphics;

    private int startRow, startCol;
    private int targetRow, targetCol;
    private long startTimeNanos;
    private int tileSize;

    private double currentX, currentY;

    public State(int startRow, int startCol, int targetRow, int targetCol,
                 int tileSize, PhysicsData physics, GraphicsData graphics) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.tileSize = tileSize;
        this.physics = physics;
        this.graphics = graphics;
        reset("idle", null);
    }

    public void reset(String state, int[] to) {
        if (to != null && to.length == 2) {
            this.targetRow = to[0];
            this.targetCol = to[1];
        }
        this.currentX = startCol * tileSize;
        this.currentY = startRow * tileSize;
        this.startTimeNanos = System.nanoTime();

        if (graphics != null) graphics.reset(state, to);
        if (physics != null) physics.reset(state, to);
    }


    public void update() {
        updatePhysicsPosition();

        if (graphics != null) graphics.update();
        if (physics != null) physics.update();

        if (isMovementFinished()){
            startRow = targetRow;
            startCol = targetCol;
        }
    }

    private void updatePhysicsPosition() {
        if (physics == null) return;

        double speed = physics.getSpeedMetersPerSec();
        long now = System.nanoTime();
        double elapsedSec = (now - startTimeNanos) / 1_000_000_000.0;

        double dx = (targetCol - startCol) * tileSize;
        double dy = (targetRow - startRow) * tileSize;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        if (totalDistance == 0 || speed == 0) return;

        double distanceSoFar = Math.min(speed * elapsedSec, totalDistance);
        double t = distanceSoFar / totalDistance;

        currentX = (startCol * tileSize) + dx * t;
        currentY = (startRow * tileSize) + dy * t;
    }

    public boolean isMovementFinished() {
        if (physics == null) return true;

        double speed = physics.getSpeedMetersPerSec();
        long now = System.nanoTime();
        double elapsedSec = (now - startTimeNanos) / 1_000_000_000.0;

        double dx = (targetCol - startCol) * tileSize;
        double dy = (targetRow - startRow) * tileSize;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        double distanceSoFar = speed * elapsedSec;
        return distanceSoFar >= totalDistance;
    }

    public Point2D.Double getCurrentPosition() {
        return new Point2D.Double(currentX, currentY);
    }

    public Point getBoardPosition() {
        return new Point((int)(currentX / tileSize), (int)(currentY / tileSize));
    }

    public int getCurrentRow() {
        return (int)(currentY / tileSize);
    }

    public int getCurrentCol() {
        return (int)(currentX / tileSize);
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public PhysicsData getPhysics() {
        return physics;
    }

    public GraphicsData getGraphics() {
        return graphics;
    }

    public int getTileSize() {
        return tileSize;
    }
}
