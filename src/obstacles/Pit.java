package obstacles;

import game.Player;
import types.Vector;

import java.awt.*;
import java.awt.geom.Ellipse2D;

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
        player.kill();
    }

    @Override
    public void draw(Graphics2D g, int tileSize) {
        Vector offset = position.copy();
        offset.scale(tileSize);

        int width = tileSize * 4 / 5;
        int height = tileSize * 7 / 10;

        offset.add(tileSize / 10, tileSize * 3 / 20);

        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(offset.X(), offset.Y(), width, height));

        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.PLAIN, tileSize / 5));
        offset.add(tileSize / 5 * 1, tileSize / 5 * 2);
        g.drawString("Pit", offset.X(), offset.Y());
    }
}