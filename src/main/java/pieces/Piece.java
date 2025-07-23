package pieces;

import state.State;

import java.awt.geom.Point2D;
import java.io.IOException;
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

    public Piece(String type, Map<String, State> states, String initialState, int row, int col) throws IOException {
        id = row+","+col;
        this.states = states;
        this.currentStateName = initialState;
        this.currentState = states.get(initialState);
        this.row = row;
        this.col = col;
        this.type = type;

        moves = new Moves(type);
    }

    public String getId() {
        return id;
    }

    public void setState(String newStateName) {
        if (states.containsKey(newStateName) && !newStateName.equals(currentStateName)) {
            currentStateName = newStateName;
            currentState = states.get(newStateName);

            // העבר את המיקום הנוכחי כדי שלא יאתחל למיקום לא נכון
            int[] pos = new int[]{this.row, this.col};
            currentState.reset(newStateName, pos);
        } else if (!states.containsKey(newStateName)) {
            // טיפול במקרה שאין סטייט כזה - רק הדפסה, לא מעבר ל-idle
            System.err.println("State '" + newStateName + "' not found!");
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void update() {
        currentState.update();

        if (currentState.isActionFinished()) {
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

    public void jump(){
        if (states.containsKey("jump")) {
            currentStateName = "jump";
            currentState = states.get("jump");
            currentState.reset("jump", new int[]{row, col});
        } else {
            System.err.println("Missing 'jump' state!");
        }
    }

//    public void draw(Graphics g, int x, int y, int width, int height) {
//        BufferedImage sprite = currentState.getGraphics().getCurrentFrame();
//        if (sprite == null) return;
//
//        if (currentStateName.equals("move")) {
//            // בזמן תנועה - המר את המיקום הפיקסלי למערכת הקואורדינטות האמיתית
//            Point2D.Double pos = getCurrentPixelPosition();
//
//            // המר מ-TILE_SIZE=64 לגודל המשבצת האמיתי
//            double realX = (pos.getX() / 64.0) * width;
//            double realY = (pos.getY() / 64.0) * height;
//
//            g.drawImage(sprite, (int) realX, (int) realY, width, height, null);
//        } else {
//            // כשלא בתנועה - השתמש במיקום המדויק של המשבצת
//            g.drawImage(sprite, x, y, width, height, null);
//        }
//    }

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
}
