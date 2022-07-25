package obstacles;

import game.Player;
import types.Vector;

public class Gold extends Obstacle {
    public Gold(Vector vector) {
        super(vector);
    }

    @Override
    public void adjacentEffect(Player player) {
        player.setShiny();
    }

    @Override
    public void onTopEffect(Player player) {
        if(!player.alreadyCollected(this)) {
            player.acquire(this);
        }
    }
}