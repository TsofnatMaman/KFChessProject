package state;

import interfaces.*;
import pieces.Position;
import java.awt.*;
import java.awt.geom.Point2D;
import utils.LogUtils; // import the logger

/**
 * Represents the state of a piece, including physics and graphics.
 */
public class State implements IState {
    private String name;
    private IPhysicsData physics;
    private IGraphicsData graphics;

    private Position startPos;
    private Position targetPos;
    private long startTimeNanos;
    private final double tileSize;

    /**
     * Constructs a State object representing a piece's state.
     */
    public State(String name, Position startPos, Position targetPos,
                 double tileSize, IPhysicsData physics, IGraphicsData graphics) {
        this.name = name;
        this.startPos = startPos;
        this.targetPos = targetPos;
        this.tileSize = tileSize;
        this.physics = physics;
        this.graphics = graphics;
        reset(EState.IDLE, startPos, null);
    }

    /**
     * Resets the state to a new action, updating physics and graphics.
     * @param state The new state.
     * @param from The starting position.
     * @param to The target position.
     */
    @Override
    public void reset(EState state, Position from, Position to) {
        if (from != null && to != null) {
            this.startPos = new Position(from.getRow(), from.getCol());
            this.targetPos = new Position(to.getRow(), to.getCol());
        }

        this.startTimeNanos = System.nanoTime();

        if (graphics != null) graphics.reset(state, startPos);
        if (physics != null) physics.reset(state, startPos, targetPos, tileSize, startTimeNanos);
    }

    /**
     * Updates the physics and graphics for the current state.
     */
    @Override
    public void update() {
        if (graphics != null) graphics.update();
        if (physics != null) physics.update();

        if (isActionFinished()) {
            startPos = targetPos;
        }
    }

    /**
     * Checks if the current action (move, jump, rest) is finished.
     * @return true if finished, false otherwise
     */
    @Override
    public boolean isActionFinished() {
        switch (name) {
            case "move":
                return physics.isMovementFinished();
            case "jump":
                boolean finished = graphics != null && graphics.isAnimationFinished();
                if (finished) {
                    System.out.println("Jump animation finished, transitioning to: " + physics.getNextStateWhenFinished());
                    LogUtils.logDebug("Jump animation finished, transitioning to: " + physics.getNextStateWhenFinished());
                }
                return finished;
            case "short_rest":
            case "long_rest":
                boolean restFinished = graphics != null && graphics.isAnimationFinished();
                if (restFinished) {
                    System.out.println(name + " animation finished");
                    LogUtils.logDebug(name + " animation finished");
                }
                return restFinished;
            default:
                // By default, check physics if available, otherwise graphics
                if (physics != null)
                    return physics.isMovementFinished();
                else if (graphics != null)
                    return graphics.isAnimationFinished();
                else
                    return true;
        }
    }

    @Override
    public int getStartCol() {
        return startPos.getCol();
    }

    @Override
    public int getStartRow() {
        return startPos.getRow();
    }

    @Override
    public Point2D.Double getCurrentPosition() {
        return new Point2D.Double(physics.getCurrentX(), physics.getCurrentY());
    }

    @Override
    public Point getBoardPosition() {
        return new Point((int) (physics.getCurrentX() / tileSize), (int) (physics.getCurrentY() / tileSize));
    }

    @Override
    public int getCurrentRow() {
        return (int) (physics.getCurrentY() / tileSize);
    }

    @Override
    public int getCurrentCol() {
        return (int) (physics.getCurrentX() / tileSize);
    }

    @Override
    public int getTargetRow() {
        return targetPos.getRow();
    }

    @Override
    public int getTargetCol() {
        return targetPos.getCol();
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
