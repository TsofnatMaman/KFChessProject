package interfaces;

public enum EState {
    IDLE("idle"),
    JUMP("jump"),
    MOVE("move"),
    LONG_REST("long_rest"),
    SHORT_REST("short_rest");

    private String name;

    EState(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
