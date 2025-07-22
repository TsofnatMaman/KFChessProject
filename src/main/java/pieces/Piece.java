package pieces;

import state.State;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Piece {
    private Map<String, State> states;  // כל הסטייטים של הכלי
    private String currentStateName;    // השם של הסטייט הנוכחי
    private State currentState;

    private int row;  // מיקום הלוח (שורה)
    private int col;  // מיקום הלוח (עמודה)

    public Piece(Map<String, State> states, String initialState, int row, int col) {
        this.states = states;
        this.currentStateName = initialState;
        this.currentState = states.get(initialState);
        this.row = row;
        this.col = col;
    }

    public void setState(String newStateName) {
        if (states.containsKey(newStateName) && !newStateName.equals(currentStateName)) {
            currentStateName = newStateName;
            currentState = states.get(newStateName);

            // העבר את המיקום הנוכחי כדי שלא יאתחל למיקום לא נכון
            int[] pos = new int[]{this.row, this.col};
            currentState.reset(newStateName, pos);
        }
    }

    public void update() {
        currentState.update();

        // אם התנועה הסתיימה, עדכן את המיקום הלוגי ועבור לסטייט הבא
        if (currentState.isMovementFinished()) {
            this.row = currentState.getTargetRow();
            this.col = currentState.getTargetCol();

            String nextState = currentState.getPhysics().getNextStateWhenFinished();
            setState(nextState);
            return; // עצור כאן כדי לא לעבור שוב סטייט בפעם אחת
        }

        // מעבר אוטומטי לסטייט הבא אם האנימציה לא לולאה והסתיימה
        if (currentState.getGraphics() != null
                && currentState.getGraphics().isAnimationFinished()) {
            String nextState = currentState.getPhysics().getNextStateWhenFinished();
            setState(nextState);
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

    public void draw(Graphics g, int x, int y, int width, int height) {
        BufferedImage sprite = currentState.getGraphics().getCurrentFrame();
        if (sprite == null) return;

        // צייר לפי המיקום המדויק בפיקסלים (לא רק לפי תאי הלוח)
        Point2D.Double pos = getCurrentPixelPosition();
        g.drawImage(sprite, (int) pos.getX(), (int) pos.getY(), width, height, null);
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
}
