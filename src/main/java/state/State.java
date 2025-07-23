package state;

import java.awt.*;
import java.awt.geom.Point2D;

public class State {
    private String name;
    private PhysicsData physics;
    private GraphicsData graphics;

    private int startRow, startCol;
    private int targetRow, targetCol;
    private long startTimeNanos;
    private final int tileSize;

    private double currentX, currentY;

    public State(String name, int startRow, int startCol, int targetRow, int targetCol,
                 int tileSize, PhysicsData physics, GraphicsData graphics) {
        this.name = name;
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

        if (isActionFinished()){
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

    private boolean isMovementFinished() {
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

    public boolean isActionFinished() {
        switch (name) {
            case "move":
                return isMovementFinished();
            case "jump":
                boolean finished = graphics != null && graphics.isAnimationFinished();
                if (finished) {
                    System.out.println("Jump animation finished, transitioning to: " + physics.getNextStateWhenFinished());
                }
                return finished;
            case "short_rest":
            case "long_rest":
                boolean restFinished = graphics != null && graphics.isAnimationFinished();
                if (restFinished) {
                    System.out.println(name + " animation finished");
                }
                return restFinished;
            default:
                // כברירת מחדל, אם יש פיזיקה - נבדוק לפי תנועה, אחרת לפי אנימציה
                if (physics != null)
                    return isMovementFinished();
                else if (graphics != null)
                    return graphics.isAnimationFinished();
                else
                    return true;
        }
    }

    public int getStartCol() {
        return startCol;
    }

    public int getStartRow() {
        return startRow;
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
}
