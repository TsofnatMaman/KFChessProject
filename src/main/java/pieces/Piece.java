package pieces;

import interfaces.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Piece implements IPiece {
    private final String id;
    private final String type;
    private Map<EState, IState> states;
    private EState currentStateName;
    private IState currentState;

    private final Moves moves;

    private Position pos;

    private boolean wasCaptured = false;

    public Piece(String type, Map<EState, IState> states, EState initialState, Position pos) throws IOException {
        id = pos.getRow() + "," + pos.getCol();
        this.states = states;
        this.currentStateName = initialState;
        this.currentState = states.get(initialState);
        this.pos = pos;

        this.type = type;

        moves = new Moves(type);
    }

    @Override
    public int getPlayer() {
        List<List<Integer>> rowsOfPlayer = List.of(
                List.of(0, 1), // שחקן 0
                List.of(6, 7)  // שחקן 1
        );

        if (rowsOfPlayer.get(0).contains(Integer.parseInt(getId().charAt(0)+"")))
            return 0;
        else
            return 1;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setState(EState newStateName) {
        if (states.containsKey(newStateName) && !newStateName.equals(currentStateName)) {
            currentStateName = newStateName;
            currentState = states.get(newStateName);

            // Update state.startPos before reset
            currentState.reset(newStateName, pos, pos);
        } else if (!states.containsKey(newStateName)) {
            System.err.println("State '" + newStateName + "' not found!");
        }
    }


    @Override
    public IState getCurrentState() {
        return currentState;
    }

    @Override
    public void update() {
        currentState.update();

        if (currentState.isActionFinished()) {
            // עדכון המיקום הלוגי רק אחרי שהפעולה הסתיימה
            pos = new Position(currentState.getTargetRow(), currentState.getTargetCol());

            EState nextState = currentState.getPhysics().getNextStateWhenFinished();

            setState(nextState);
            return; // עצור כדי לא לעבור שוב סטייט בפעם אחת
        }

        // מעבר אוטומטי אם האנימציה הסתיימה
        if (currentState.getGraphics() != null && currentState.getGraphics().isAnimationFinished()) {
            EState nextState = currentState.getPhysics().getNextStateWhenFinished();
            setState(nextState);
        }
    }


    @Override
    public void move(Position to) {
        if (states.containsKey(EState.MOVE)) {
            currentStateName = EState.MOVE;
            currentState = states.get(EState.MOVE);
            currentState.reset(EState.MOVE, pos, to);
        } else {
            System.err.println("Missing 'move' state!");
        }
    }

    @Override
    public void jump() {
        if (states.containsKey(EState.JUMP)) {
            currentStateName = EState.JUMP;
            currentState = states.get(EState.JUMP);
            currentState.reset(EState.JUMP,pos, pos);
        } else {
            System.err.println("Missing 'jump' state!");
        }
    }

    @Override
    public boolean isCaptured() {
        return wasCaptured;
    }

    @Override
    public void markCaptured() {
        this.wasCaptured = true;
    }

    @Override
    public void setLogicalPosition(Position pos) {
        this.pos = pos;
    }

    @Override
    public int getRow() {
        return pos.getRow();
    }

    @Override
    public int getCol() {
        return pos.getCol();
    }

    @Override
    public EState getCurrentStateName() {
        return currentStateName;
    }

    @Override
    public Point2D.Double getCurrentPixelPosition() {
        return currentState.getCurrentPosition();
    }

    @Override
    public Moves getMoves() {
        return moves;
    }

    @Override
    public Map<EState, IState> getStates() {
        return states;
    }

    @Override
    public boolean canMoveOver(){
        return currentStateName.isCanMoveOver();
    }
}
