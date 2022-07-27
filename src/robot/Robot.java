package robot;

import types.Bounds;
import types.Vector;

public abstract class Robot {
    protected Bounds bounds;
    protected Vector position;
    protected boolean smelly, windy, shiny;

    public Robot(Bounds b, Vector p) {
        bounds = b;
        position = p;
    }

    public boolean isSmelly() {
        return smelly;
    }

    public boolean isWindy() {
        return windy;
    }

    public boolean isShiny() {
        return shiny;
    }

    public Vector getPosition() {
        return position;
    }
}
