package events.listeners;

import events.GameEvent;
import events.IEventListener;

public class JumpsLogger implements IEventListener {
    @Override
    public void onEvent(GameEvent event) {
        if (event.type.equals(GameEvent.PIECE_MOVED)) {
            String jumpDescription = (String) event.data;
            System.out.println("Move: " + jumpDescription);
        }
    }
}
