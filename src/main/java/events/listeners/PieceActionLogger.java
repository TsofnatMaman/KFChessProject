package events.listeners;

import events.GameEvent;
import events.IEventListener;

import java.util.ArrayList;
import java.util.List;

public class PieceActionLogger implements IEventListener {

    private final List<String> actions = new ArrayList<>();

    @Override
    public void onEvent(GameEvent event) {
        String actionData = (String) event.data;
        actions.add(actionData);
        System.out.println("action: " + actionData);
    }
}
