package game;

import types.Vector;

public class Pit extends Obstacle {
    public Pit(Vector vector) {
        super(vector);
    }

    @Override
    public void adjacentEffect(Player player) {
        player.setWindy();
    }

    @Override
    public void onTopEffect(Player player) {
        player.fall();
    }
}