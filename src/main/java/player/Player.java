package player;

public class Player {
    private PlayerCursor cursor;
    private int[] pending;

    public Player(PlayerCursor pc){
        this.cursor = pc;
        pending=null;
    }

    public PlayerCursor getCursor() {
        return cursor;
    }

    public int[] getPendingFrom() {
        return pending;
    }

    public void setPendingFrom(int[] pending) {
        this.pending = pending;
    }

    public String getCurrentSelection() {
        int row = cursor.getRow();
        int col = cursor.getCol();
        return row + "," + col;
    }
}
