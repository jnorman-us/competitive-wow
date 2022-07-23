package messages;

import types.Vector;

public class AcquiredGold extends Message {
    public static final String TYPE = "AcquiredGold";

    public Vector currentPosition;
    public int goldCollected;

    public AcquiredGold(Vector position, int collected) {
        super(TYPE);
        currentPosition = position;
        goldCollected = collected;
    }
}
