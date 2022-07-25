package obstacles;

import game.Player;
import types.Vector;

public class Monster extends Obstacle {
    public Monster(Vector vector) {
        super(vector);
    }

    @Override
    public void adjacentEffect(Player player) {
        player.setSmelly();
    }

    @Override
    public void onTopEffect(Player player) {
        player.eat();
    }
}
