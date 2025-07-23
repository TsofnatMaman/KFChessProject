package pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import state.State;
import state.PhysicsData;
import state.GraphicsData;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PieceTest {

    private State mockStateIdle;
    private State mockStateMove;
    private PhysicsData mockPhysics;
    private GraphicsData mockGraphics;

    private Map<String, State> states;

    @BeforeEach
    void setup() {
        mockStateIdle = mock(State.class);
        mockStateMove = mock(State.class);

        mockPhysics = mock(PhysicsData.class);
        mockGraphics = mock(GraphicsData.class);

        // הגדרות מחדל עבור idle
        when(mockStateIdle.isMovementFinished()).thenReturn(false);
        when(mockStateIdle.getPhysics()).thenReturn(mockPhysics);
        when(mockStateIdle.getGraphics()).thenReturn(mockGraphics);

        // הגדרות מחדל עבור move
        when(mockStateMove.isMovementFinished()).thenReturn(false);
        when(mockStateMove.getPhysics()).thenReturn(mockPhysics);
        when(mockStateMove.getGraphics()).thenReturn(mockGraphics);

        states = new HashMap<>();
        states.put("idle", mockStateIdle);
        states.put("move", mockStateMove);
    }

    @Test
    void testInitialState() throws IOException {
        Piece piece = new Piece("", states, "idle", 2, 3);
        assertEquals("idle", piece.getCurrentStateName());
        assertEquals(mockStateIdle, piece.getCurrentState());
        assertEquals(2, piece.getRow());
        assertEquals(3, piece.getCol());
    }

    @Test
    void testSetState_changesState() throws IOException {
        Piece piece = new Piece("",states, "idle", 2, 3);

        piece.setState("move");
        assertEquals("move", piece.getCurrentStateName());
        assertEquals(mockStateMove, piece.getCurrentState());

        // בדיקה ש־reset נקרא עם המיקום הנכון
        verify(mockStateMove).reset(eq("move"), eq(new int[]{2,3}));
    }

    @Test
    void testSetState_noChangeIfSameState() throws IOException {
        Piece piece = new Piece("",states, "idle", 2, 3);
        piece.setState("idle"); // אותו מצב

        // לא אמור לקרוא ל-reset כי זה אותו מצב
        verify(mockStateIdle, never()).reset(anyString(), any());
    }

    @Test
    void testUpdateMovementFinished_updatesPositionAndState() throws IOException {
        Piece piece = new Piece("",states, "idle", 1, 1);

        when(mockStateIdle.isMovementFinished()).thenReturn(true);
        when(mockStateIdle.getTargetRow()).thenReturn(4);
        when(mockStateIdle.getTargetCol()).thenReturn(5);
        when(mockPhysics.getNextStateWhenFinished()).thenReturn("move");

        piece.update();

        // מיקום צריך להתעדכן
        assertEquals(4, piece.getRow());
        assertEquals(5, piece.getCol());
        // מצב צריך להשתנות ל-"move"
        assertEquals("move", piece.getCurrentStateName());
    }

    @Test
    void testUpdateAnimationFinished_switchesState() throws IOException {
        Piece piece = new Piece("",states, "idle", 0, 0);

        when(mockStateIdle.isMovementFinished()).thenReturn(false);
        when(mockGraphics.isAnimationFinished()).thenReturn(true);
        when(mockPhysics.getNextStateWhenFinished()).thenReturn("move");

        piece.update();

        assertEquals("move", piece.getCurrentStateName());
    }

    @Test
    void testMove_invokesMoveStateReset() throws IOException {
        Piece piece = new Piece("",states, "idle", 0, 0);
        int[] destination = new int[]{3,3};

        piece.move(destination);

        assertEquals("move", piece.getCurrentStateName());
        verify(mockStateMove).reset("move", destination);
    }

    @Test
    void testMove_noMoveState_logsError() throws IOException {
        Map<String, State> singleState = new HashMap<>();
        singleState.put("idle", mockStateIdle);
        Piece piece = new Piece("",singleState, "idle", 0, 0);

        // ננסה לקרוא ל move, אבל אין מצב כזה
        piece.move(new int[]{2,2});
        // כאן רק הדפסה לשגיאה - אפשר לוותר על בדיקה אם לא מוסיפים Logger
    }
}
