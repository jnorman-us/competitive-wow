package messages;

import types.Vector;

public class Died extends Message {
    public static final String TYPE = "Died";

    public Vector currentPosition;

    public Died(Vector position) {
        super(TYPE);
        currentPosition = position;
    }
}
