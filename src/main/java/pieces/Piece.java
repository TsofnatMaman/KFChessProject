package pieces;

import state.State;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Piece {
    private final String id;
    private final String type;
    private Map<String, State> states;  // כל הסטייטים של הכלי
    private String currentStateName;    // השם של הסטייט הנוכחי
    private State currentState;

    private final Moves moves;

    private int row;  // מיקום הלוח (שורה)
    private int col;  // מיקום הלוח (עמודה)

    private boolean wasCaptured = false;

    public Piece(String type, Map<String, State> states, String initialState, int row, int col) throws IOException {
        id = row + "," + col;
        this.states = states;
        this.currentStateName = initialState;
        this.currentState = states.get(initialState);
        this.row = row;
        this.col = col;
        this.type = type;

        moves = new Moves(type);
    }

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

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setState(String newStateName) {
        if (states.containsKey(newStateName) && !newStateName.equals(currentStateName)) {
            currentStateName = newStateName;
            currentState = states.get(newStateName);

            // העבר את המיקום הנוכחי כדי שלא יאתחל למיקום לא נכון
            int[] pos = new int[]{this.row, this.col};
            currentState.reset(newStateName, pos);
        } else if (!states.containsKey(newStateName)) {
            System.err.println("State '" + newStateName + "' not found!");
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void update() {
        currentState.update();

        if (currentState.isActionFinished()) {
            // עדכון המיקום הלוגי רק אחרי שהפעולה הסתיימה
            this.row = currentState.getTargetRow();
            this.col = currentState.getTargetCol();

            String nextState = currentState.getPhysics().getNextStateWhenFinished();

            // נעדכן את הסטייט הבא כמו שהוא בלי להחליף ל-idle מיד
            setState(nextState);
            return; // עצור כדי לא לעבור שוב סטייט בפעם אחת
        }

        // מעבר אוטומטי אם האנימציה הסתיימה
        if (currentState.getGraphics() != null && currentState.getGraphics().isAnimationFinished()) {
            String nextState = currentState.getPhysics().getNextStateWhenFinished();

            // אם הסטייט הבא הוא מנוחה - נחליף ל-idle כדי לאפשר תנועה
            if ("short_rest".equals(nextState) || "long_rest".equals(nextState)) {
                setState("idle");
            } else {
                setState(nextState);
            }
        }
    }



    public void move(int[] to) {
        if (states.containsKey("move")) {
            currentStateName = "move";
            currentState = states.get("move");
            currentState.reset("move", to);
        } else {
            System.err.println("Missing 'move' state!");
        }
    }

    public void jump() {
        if (states.containsKey("jump")) {
            currentStateName = "jump";
            currentState = states.get("jump");
            currentState.reset("jump", new int[]{row, col});
        } else {
            System.err.println("Missing 'jump' state!");
        }
    }

    public boolean isCaptured() {
        return wasCaptured;
    }

    public void markCaptured() {
        this.wasCaptured = true;
    }

    public void setLogicalPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    public Point2D.Double getCurrentPixelPosition() {
        return currentState.getCurrentPosition();
    }

    public Moves getMoves() {
        return moves;
    }

    public Map<String, State> getStates() {
        return states;
    }
}
