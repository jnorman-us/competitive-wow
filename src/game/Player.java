package game;

import game.Game;
import game.Gold;
import messages.AcquiredGold;
import messages.Decision;
import messages.Died;
import messages.RequestThink;
import robot.Robot;
import types.Bounds;
import types.Direction;
import types.User;
import types.Vector;

import java.util.HashSet;

public class Player extends Robot {
    private Game game;

    private User user;
    private boolean dead;
    private boolean received;
    private HashSet<Gold> goldCollected;

    public Player(Game g, User user, Vector p) {
        super(g, p);
        this.user = user;
        this.game = g;

        goldCollected = new HashSet<>();
    }

    public boolean isDead() {
        return dead;
    }

    public void kill() {
        dead = true;
    }

    public boolean hasReceived() {
        return received;
    }

    public void resetReceived() {
        received = false;
    }

    public void move(Direction direction) {
        received = true;
        position.move(direction);
        position.enforceBounds(bounds);
    }

    public void reset() {
        clearSensors();
        dead = false;
        position.set(0, 0);
        goldCollected.clear();
    }

    public RequestThink generateRequestThink() {
        return new RequestThink(position, smelly, windy, shiny);
    }

    public Vector getPosition() {
        return position;
    }

    public void setSmelly() {
        smelly = true;
    }

    public void eat() {
        kill();
        game.publisher.sendMessage(this.user, new Died(position));
    }

    public void setShiny() {
        shiny = true;;
    }

    public boolean alreadyCollected(Gold g) {
        return goldCollected.contains(g);
    }

    public int goldCollected() {
        return goldCollected.size();
    }

    public void acquire(Gold g) {
        goldCollected.add(g);
        game.publisher.sendMessage(this.user, new AcquiredGold(position, goldCollected()));
    }

    public void setWindy() {
        windy = true;
    }

    public void fall() {
        kill();
        game.publisher.sendMessage(this.user, new Died(position));
    }

    public void clearSensors() {
        smelly = false;
        windy = false;
        shiny = false;
    }
}
