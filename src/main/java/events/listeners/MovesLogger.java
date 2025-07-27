package events.listeners;

import events.GameEvent;
import events.IEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovesLogger implements IEventListener {
    private final List<String> moves = new ArrayList<>();

    @Override
    public void onEvent(GameEvent event) {
        if (event.type.equals(GameEvent.PIECE_MOVED)) {
            String moveDescription = (String) event.data;
            moves.add(moveDescription);
            System.out.println("Move: " + moveDescription);
        }
    }

    public List<String> getMoves() {
        return new ArrayList<>(moves);
    }
}
