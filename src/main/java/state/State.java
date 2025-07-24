package state;

import interfaces.*;
import pieces.Position;

import java.awt.*;
import java.awt.geom.Point2D;

public class State implements IState{
    private String name;
    private IPhysicsData physics;
    private IGraphicsData graphics;

    private Position startPos;
    private Position targetPos;
    private long startTimeNanos;
    private final double tileSize;

    private double currentX, currentY;

    public State(String name, Position startPos, Position targetPos,
                 double tileSize, IPhysicsData physics, IGraphicsData graphics) {
        this.name = name;

        this.startPos = startPos;
        this.targetPos = targetPos;

        this.tileSize = tileSize;
        this.physics = physics;
        this.graphics = graphics;
        reset(EState.IDLE,startPos, null);
    }

    @Override
    public void reset(EState state, Position from, Position to) {
        if (from != null && to != null) {
            this.startPos = new Position(from.getR(), from.getC());
            this.targetPos = new Position(to.getR(), to.getC());
        }

        this.currentX = startPos.getC() * tileSize;
        this.currentY = startPos.getR() * tileSize;
        this.startTimeNanos = System.nanoTime();

        if (graphics != null) graphics.reset(state, to);
        if (physics != null) physics.reset(state, to);
    }



    @Override
    public void update() {
        updatePhysicsPosition();

        if (graphics != null) graphics.update();
        if (physics != null) physics.update();

        if (isActionFinished()){
            startPos = targetPos;
        }
    }

    @Override
    public void updatePhysicsPosition() {
        if (physics == null) return;

        double speed = physics.getSpeedMetersPerSec();
        long now = System.nanoTime();
        double elapsedSec = (now - startTimeNanos) / 1_000_000_000.0;

        double dx = (targetPos.dy(startPos)) * tileSize;
        double dy = (targetPos.dx(startPos)) * tileSize;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        if (totalDistance == 0 || speed == 0) return;

        double distanceSoFar = Math.min(speed * elapsedSec, totalDistance);
        double t = distanceSoFar / totalDistance;

        currentX = (startPos.getC() * tileSize) + dx * t;
        currentY = (startPos.getR() * tileSize) + dy * t;
    }

    @Override
    public boolean isMovementFinished() {
        if (physics == null) return true;

        double speed = physics.getSpeedMetersPerSec();
        long now = System.nanoTime();
        double elapsedSec = (now - startTimeNanos) / 1_000_000_000.0;

        double dx = targetPos.dy(startPos) * tileSize;
        double dy = targetPos.dx(startPos) * tileSize;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        double distanceSoFar = speed * elapsedSec;
        return distanceSoFar >= totalDistance;
    }

    @Override
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

    @Override
    public int getStartCol() {
        return startPos.getC();
    }

    @Override
    public int getStartRow() {
        return startPos.getR();
    }

    @Override
    public Point2D.Double getCurrentPosition() {
        return new Point2D.Double(currentX, currentY);
    }

    @Override
    public Point getBoardPosition() {
        return new Point((int)(currentX / tileSize), (int)(currentY / tileSize));
    }

    @Override
    public int getCurrentRow() {
        return (int)(currentY / tileSize);
    }

    @Override
    public int getCurrentCol() {
        return (int)(currentX / tileSize);
    }

    @Override
    public int getTargetRow() {
        return targetPos.getR();
    }

    @Override
    public int getTargetCol() {
        return targetPos.getC();
    }

    @Override
    public IPhysicsData getPhysics() {
        return physics;
    }

    @Override
    public IGraphicsData getGraphics() {
        return graphics;
    }
}
