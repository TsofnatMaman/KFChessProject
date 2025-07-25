package interfaces;

public enum EState {
    IDLE("idle", true, false),
    JUMP("jump", false, false),
    MOVE("move", false, true),
    LONG_REST("long_rest", false, true),
    SHORT_REST("short_rest", false, true);

    private String name;
    private boolean canAction;
    private boolean canMoveOver;

    EState(String name, boolean canAction, boolean canMoveOver){
        this.name = name;
        this.canAction = canAction;
        this.canMoveOver = canMoveOver;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isCanAction(){
        return canAction;
    }

    public boolean isCanMoveOver() {
        return canMoveOver;
    }
}
