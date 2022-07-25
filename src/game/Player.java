package game;

import obstacles.Gold;
import messages.AcquiredGold;
import messages.Died;
import messages.RequestThink;
import robot.Robot;
import types.Direction;
import types.User;
import types.Vector;

import java.awt.*;
import java.awt.geom.Ellipse2D;
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
        game.statusListener.playerDied(user);
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
        game.statusListener.playerGoldUpdate(user, goldCollected());
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

    public void draw(Graphics2D g, int tileSize, int slot) {
        int size = tileSize / 2;
        Vector offset = getPosition().copy();
        offset.scale(tileSize);
        if(slot == 1) {
            offset.add(size, 0);
        } else if(slot == 2) {
            offset.add(0, size);
        } else if(slot >= 3) {
            offset.add(size, size);
        }
        if(!isDead()) {
            g.setColor(Color.BLUE);
            g.fill(new Ellipse2D.Double(offset.X(), offset.Y(), size, size));
        } else {
            offset.add(size * 1 / 6, 0);
            g.setColor(Color.GRAY);
            g.fillRect(offset.X(), offset.Y(), size * 2 / 3, size);
            offset.add(size * -1 / 6, 0);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.PLAIN, tileSize / 6));
        offset.add(tileSize / 5 * 0, tileSize / 10 * 3);
        g.drawString(user.getUsername().substring(0, 5), offset.X(), offset.Y());
    }
}
