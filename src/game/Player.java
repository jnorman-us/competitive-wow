package game;

import obstacles.Gold;
import messages.AcquiredGold;
import messages.Died;
import messages.RequestThink;
import obstacles.Monster;
import obstacles.Obstacle;
import robot.Robot;
import types.Action;
import types.User;
import types.Vector;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;

public class Player extends Robot {
    private Game game;

    private User user;
    private int score;
    private boolean dead;
    private boolean received;
    private HashSet<Monster> monstersKilled;
    private HashSet<Gold> goldCollected;

    public Player(Game g, User user, Vector p) {
        super(g, p);
        this.user = user;
        this.game = g;

        goldCollected = new HashSet<>();
        monstersKilled = new HashSet<>();
    }

    private void addToScore(int toAdd) {
        score += toAdd;
        game.statusListener.playerScoreUpdate(user, score);
    }

    public boolean isDead() {
        return dead;
    }
    public void kill() {
        dead = true;
        addToScore(-100);
        game.publisher.sendMessage(this.user, new Died(position));
        game.statusListener.playerDied(user);
    }
    public boolean hasReceived() {
        return received;
    }
    public void resetReceived() {
        received = false;
    }
    public void reset() {
        clearSensors();
        dead = false;
        position.set(0, 0);
        goldCollected.clear();
        monstersKilled.clear();
    }

    public void act(Action direction) {
        received = true;
        switch(direction) {
            case MOVE_UP:
            case MOVE_DOWN:
            case MOVE_LEFT:
            case MOVE_RIGHT:
                position.move(direction);
                position.enforceBounds(bounds);
                addToScore(-10); // all actions incur a 10pt cost
                return;
        }

        Vector shootAt = getPosition().copy();
        switch(direction) {
            case SHOOT_UP:
                shootAt.add(0, -1);
                break;
            case SHOOT_DOWN:
                shootAt.add(0, 1);
                break;
            case SHOOT_LEFT:
                shootAt.add(-1, 0);
                break;
            case SHOOT_RIGHT:
                shootAt.add(1, 0);
                break;
        }
        addToScore(-30); // all shooting incur a 200pt cost

        Monster shotMonster = game.getMonsterAt(shootAt);
        if(shotMonster != null) {
            killMonster(shotMonster);
        }
    }

    public boolean alreadyKilled(Monster m) { return monstersKilled.contains(m); }
    public int monstersKilled() { return monstersKilled.size(); }
    public void killMonster(Monster m) {
        monstersKilled.add(m);
        addToScore(70);
    }

    public boolean alreadyCollected(Gold g) {
        return goldCollected.contains(g);
    }
    public int goldCollected() {
        return goldCollected.size();
    }
    public void acquire(Gold g) {
        goldCollected.add(g);
        addToScore(100);
        game.publisher.sendMessage(this.user, new AcquiredGold(position, goldCollected()));
        game.statusListener.playerGoldUpdate(user, goldCollected());
    }

    public void setSmelly() {
        smelly = true;
    }
    public void setWindy() {
        windy = true;
    }
    public void setShiny() {
        shiny = true;
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
