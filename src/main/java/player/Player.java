package player;

public class Player {
    private int id;
    private PlayerCursor cursor;
    private int[] pending;
    private static int mone=0;

    public Player(PlayerCursor pc){
        id = mone++;
        this.cursor = pc;
        pending=null;
    }

    public int getId() {
        return id;
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
