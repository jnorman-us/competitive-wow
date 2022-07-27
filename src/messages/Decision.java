package messages;

import types.Action;

public class Decision extends Message {
    public static final String TYPE = "Decision";

    public Action action;

    public Decision(Action a) {
        super(TYPE);
        action = a;
    }

    public Action getAction() {
        return action;
    }
}
