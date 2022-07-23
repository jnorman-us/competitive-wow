package messages;

import types.Direction;

public class Decision extends Message {
    public static final String TYPE = "Decision";

    public Direction toMove;

    public Decision(Direction t) {
        super(TYPE);
        toMove = t;
    }

    public Direction movement() {
        return toMove;
    }
}
