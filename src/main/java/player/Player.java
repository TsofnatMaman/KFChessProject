package player;

public class Player {
    private PlayerCursor cursor;

    public Player(PlayerCursor pc){
        this.cursor = pc;
    }

    public PlayerCursor getCursor() {
        return cursor;
    }
}
