package state;

import interfaces.*;
import pieces.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    private State state;
    private IPhysicsData physics;
    private IGraphicsData graphics;
    private Position startPos;
    private Position targetPos;
    private double tileSize = 50.0;

    // Dummy implementations for physics and graphics to control behavior in tests
    private static class DummyPhysics implements IPhysicsData {
        private boolean finished = false;
        private double currentX = 0;
        private double currentY = 0;

        @Override
        public double getSpeedMetersPerSec() { return 1; }
        @Override
        public void setSpeedMetersPerSec(double speedMetersPerSec) {}
        @Override
        public EState getNextStateWhenFinished() { return EState.IDLE; }
        @Override
        public void setNextStateWhenFinished(EState nextStateWhenFinished) {}
        @Override
        public void reset(EState state, Position startPos, Position to, double tileSize, long startTimeNanos) {
            currentX = startPos.getCol() * tileSize;
            currentY = startPos.getRow() * tileSize;
            finished = false;
        }
        @Override
        public void update() {
            finished = true;
        }
        @Override
        public boolean isMovementFinished() { return finished; }
        @Override
        public double getCurrentX() { return currentX; }
        @Override
        public double getCurrentY() { return currentY; }
    }

    private static class DummyGraphics implements IGraphicsData {
        private boolean animationFinished = false;
        @Override
        public void reset(EState state, Position to) {}
        @Override
        public void update() { animationFinished = true; }
        @Override
        public boolean isAnimationFinished() { return animationFinished; }
        @Override
        public int getCurrentNumFrame() { return 0; }
        @Override
        public int getTotalFrames() { return 1; }
        @Override
        public double getFramesPerSec() { return 1; }
        @Override
        public boolean isLoop() { return false; }
        @Override
        public java.awt.image.BufferedImage getCurrentFrame() { return null; }
    }

    @BeforeEach
    void setup() {
        startPos = new Position(1, 1);
        targetPos = new Position(2, 2);
        physics = new DummyPhysics();
        graphics = new DummyGraphics();
        state = new State(EState.MOVE, startPos, targetPos, tileSize, physics, graphics);
    }

    @Test
    void testInitialPositions() {
        assertEquals(startPos.getRow(), state.getStartRow());
        assertEquals(startPos.getCol(), state.getStartCol());
        assertEquals(targetPos.getRow(), state.getTargetRow());
        assertEquals(targetPos.getCol(), state.getTargetCol());
    }

    @Test
    void testUpdateAndActionFinished() {
        // Initially not finished because DummyPhysics sets finished on update()
        assertFalse(state.isActionFinished());
        state.update();
        assertTrue(state.isActionFinished());
    }

    @Test
    void testCurrentPosition() {
        state.update();  // calls physics.update()
        Point2D.Double pos = state.getCurrentPosition();
        assertEquals(physics.getCurrentX(), pos.x);
        assertEquals(physics.getCurrentY(), pos.y);
    }

    @Test
    void testResetChangesPositions() {
        Position newStart = new Position(3, 3);
        Position newTarget = new Position(4, 4);
        state.reset(EState.JUMP, newStart, newTarget);
        assertEquals(newStart.getRow(), state.getStartRow());
        assertEquals(newStart.getCol(), state.getStartCol());
        assertEquals(newTarget.getRow(), state.getTargetRow());
        assertEquals(newTarget.getCol(), state.getTargetCol());
    }
}
