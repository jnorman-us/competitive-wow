package robot;

import types.Bounds;
import types.Direction;
import types.Vector;

public abstract class Robot {
    protected Bounds bounds;
    protected Vector position;
    protected boolean smelly, windy, shiny;

    public Robot(Bounds b, Vector p) {
        bounds = b;
        position = p;
    }

    protected boolean isSmelly() {
        return smelly;
    }

    protected boolean isWindy() {
        return windy;
    }

    protected boolean isShiny() {
        return shiny;
    }
}
