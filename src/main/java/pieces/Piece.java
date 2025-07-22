package pieces;

import state.State;

import java.awt.*;
import java.awt.geom.Point2D;
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
            currentState.reset();  // מאפס את האנימציה כשעוברים סטייט
        }
    }

    public void update() {
        currentState.update();
    }

    public void draw(Graphics g, int x, int y, int width, int height) {
        Image sprite = currentState.getGraphics().getCurrentFrame();
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
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
}
