package obstacles;

import game.Player;
import types.Vector;

import java.awt.*;

public abstract class Obstacle {
    protected Vector position;

    protected Obstacle() {

    }

    public Obstacle(Vector p) {
        position = p;
    }

    public abstract void adjacentEffect(Player player);
    public abstract void onTopEffect(Player player);
    public abstract void draw(Graphics2D g, int tileSize);

    public void calculateOnTop(Player player) {
        if(position.checkAdjacent(player.getPosition())) {
            adjacentEffect(player);
        } else if(position.equals(player.getPosition())) {
            onTopEffect(player);
        }
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
