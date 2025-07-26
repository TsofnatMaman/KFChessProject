package pieces;

import interfaces.*;
import pieces.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dummy implementation of IState for testing Piece class.
 */
class DummyState implements IState {
    private EState stateName;
    private boolean finished = false;
    private Position from;
    private Position to;
    private IPhysicsData physicsData;
    private IGraphicsData graphicsData;

    public DummyState(EState stateName) {
        this.stateName = stateName;
        this.physicsData = new DummyPhysicsData();
        this.graphicsData = new DummyGraphicsData();
    }

    @Override
    public void reset(EState state, Position from, Position to) {
        this.stateName = state;
        this.from = from;
        this.to = to;
        this.finished = false;
    }

    @Override
    public void update() {
        // Simulate finishing after update for testing
        finished = true;
    }

    @Override
    public boolean isActionFinished() {
        return finished;
    }

    @Override
    public int getStartCol() {
        return from != null ? from.getCol() : 0;
    }

    @Override
    public int getStartRow() {
        return from != null ? from.getRow() : 0;
    }

    @Override
    public Point2D.Double getCurrentPosition() {
        return new Point2D.Double(to != null ? to.getCol() : 0, to != null ? to.getRow() : 0);
    }

    @Override
    public java.awt.Point getBoardPosition() {
        return new java.awt.Point(to != null ? to.getCol() : 0, to != null ? to.getRow() : 0);
    }

    @Override
    public int getCurrentRow() {
        return to != null ? to.getRow() : 0;
    }

    @Override
    public int getCurrentCol() {
        return to != null ? to.getCol() : 0;
    }

    @Override
    public int getTargetRow() {
        return to != null ? to.getRow() : 0;
    }

    @Override
    public int getTargetCol() {
        return to != null ? to.getCol() : 0;
    }

    @Override
    public IPhysicsData getPhysics() {
        return physicsData;
    }

    @Override
    public IGraphicsData getGraphics() {
        return graphicsData;
    }
}

/**
 * Dummy implementation of IPhysicsData.
 */
class DummyPhysicsData implements IPhysicsData {
    private EState nextState = EState.IDLE;

    @Override
    public double getSpeedMetersPerSec() {
        return 0;
    }

    @Override
    public void setSpeedMetersPerSec(double speedMetersPerSec) {}

    @Override
    public EState getNextStateWhenFinished() {
        return nextState;
    }

    @Override
    public void setNextStateWhenFinished(EState nextStateWhenFinished) {
        this.nextState = nextStateWhenFinished;
    }

    @Override
    public void reset(EState state, Position startPos, Position to, double tileSize, long startTimeNanos) {}

    @Override
    public void update() {}

    @Override
    public boolean isMovementFinished() {
        return true;
    }

    @Override
    public double getCurrentX() {
        return 0;
    }

    @Override
    public double getCurrentY() {
        return 0;
    }
}

/**
 * Dummy implementation of IGraphicsData.
 */
class DummyGraphicsData implements IGraphicsData {
    @Override
    public void reset(EState state, Position to) {}

    @Override
    public void update() {}

    @Override
    public boolean isAnimationFinished() {
        return true;
    }

    @Override
    public int getCurrentNumFrame() {
        return 0;
    }

    @Override
    public int getTotalFrames() {
        return 1;
    }

    @Override
    public double getFramesPerSec() {
        return 1;
    }

    @Override
    public boolean isLoop() {
        return false;
    }

    @Override
    public java.awt.image.BufferedImage getCurrentFrame() {
        return null;
    }
}


public class PieceTest {

    private Piece piece;
    private Map<EState, IState> mockStates;

    @BeforeEach
    public void setUp() throws IOException {
        mockStates = new HashMap<>();
        mockStates.put(EState.IDLE, new DummyState(EState.IDLE));
        mockStates.put(EState.MOVE, new DummyState(EState.MOVE));
        mockStates.put(EState.JUMP, new DummyState(EState.JUMP));

        // השתמש בסוג כלי תקין מהמערכת שלך, לדוגמה "PB" (Pawn Black)
        piece = new Piece("PB", mockStates, EState.IDLE, new Position(2, 3));
    }

    @Test
    public void testInitialState() {
        assertEquals(EState.IDLE, piece.getCurrentStateName());
        assertEquals(2, piece.getRow());
        assertEquals(3, piece.getCol());
    }

    @Test
    public void testSetState() {
        piece.setState(EState.MOVE);
        assertEquals(EState.MOVE, piece.getCurrentStateName());
    }

    @Test
    public void testMoveUpdatesStateAndPosition() {
        Position target = new Position(4, 5);
        piece.move(target);
        assertEquals(EState.MOVE, piece.getCurrentStateName());

        // העדכון יעשה ב-update()
        piece.update();

        assertEquals(target.getRow(), piece.getRow());
        assertEquals(target.getCol(), piece.getCol());
    }

    @Test
    public void testJumpUpdatesState() {
        piece.jump();
        assertEquals(EState.JUMP, piece.getCurrentStateName());

        piece.update();
        // אחרי update, מצב אמור לחזור ל IDLE כי ב DummyPhysicsData מוגדר כך
        assertEquals(EState.IDLE, piece.getCurrentStateName());
    }

    @Test
    public void testCapture() {
        assertFalse(piece.isCaptured());
        piece.markCaptured();
        assertTrue(piece.isCaptured());
    }

    @Test
    public void testGetMoves() {
        assertNotNull(piece.getMoves());
        assertFalse(piece.getMoves().getMoves().isEmpty());
    }
}
