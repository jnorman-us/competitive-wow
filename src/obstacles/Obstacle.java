package obstacles;

import game.Player;
import types.Vector;

public abstract class Obstacle {
    private Vector position;

    protected Obstacle() {

    }

    public Obstacle(Vector p) {
        position = p;
    }

    public abstract void adjacentEffect(Player player);
    public abstract void onTopEffect(Player player);

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
