package obstacles;

import game.Player;
import types.Vector;

import java.awt.*;

public class Gold extends Obstacle {
    public Gold(Vector vector) {
        super(vector);
    }

    @Override
    public void adjacentEffect(Player player) {
        if(!player.alreadyCollected(this)) {
            player.setShiny();
        }
    }

    @Override
    public void onTopEffect(Player player) {
        if(!player.alreadyCollected(this)) {
            player.acquire(this);
        }
    }

    @Override
    public void draw(Graphics2D g, int tileSize) {
        Vector offset = position.copy();
        offset.scale(tileSize);

        int width = tileSize * 4 / 5;
        int height = tileSize * 2 / 5;

        offset.add(tileSize / 10, tileSize * 3 / 10);

        g.setColor(Color.YELLOW);
        g.fillRect(offset.X(), offset.Y(), width, height);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier New", Font.PLAIN, tileSize / 5));
        offset.add(tileSize / 20 * 3, tileSize / 20 * 5);
        g.drawString("Gold", offset.X(), offset.Y());
    }
}