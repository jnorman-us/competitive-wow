package messages;

import types.Vector;

public class RequestThink extends Message {
    public static final String TYPE = "RequestThink";

    public Vector currentPosition;
    public boolean smelly, windy, shiny;

    public RequestThink(Vector p, boolean smelly, boolean windy, boolean shiny) {
        super(TYPE);
        currentPosition = new Vector(p);
        this.smelly = smelly;
        this.windy = windy;
        this.shiny = shiny;
    }
}
