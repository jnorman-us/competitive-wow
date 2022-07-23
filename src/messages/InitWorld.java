package messages;

import types.Bounds;
import types.Vector;

public class InitWorld extends Message implements Bounds {
    public static final String TYPE = "InitWorld";

    public Vector bounds;

    public InitWorld(Bounds b) {
        super(TYPE);

        bounds = new Vector(b.high());
    }

    public Vector high() {
        return bounds;
    }
}
