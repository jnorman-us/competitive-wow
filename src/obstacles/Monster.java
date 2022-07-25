package obstacles;

import game.Player;
import types.Vector;

import java.awt.*;
import java.awt.geom.Ellipse2D;

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

    @Override
    public void draw(Graphics2D g, int tileSize) {
        Vector offset = position.copy();
        offset.scale(tileSize);

        int width = tileSize * 4 / 5;
        int height = tileSize * 4 / 5;

        offset.add(tileSize / 10, tileSize / 10);

        g.setColor(Color.decode("#835C3B"));
        g.fill(new Ellipse2D.Double(offset.X(), offset.Y(), width, height));

        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier New", Font.PLAIN, tileSize / 5));
        offset.add(tileSize / 20 * -1, tileSize / 20 * 9);
        g.drawString("Willump", offset.X(), offset.Y());
    }
}
