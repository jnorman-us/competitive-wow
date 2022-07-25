package ui;

import game.Player;
import obstacles.Obstacle;
import types.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;

public class Canvas extends JPanel {
    private static final int TILE_SIZE = 80;

    private EntityDirectory directory;

    private Vector canvasDimensions;

    public Canvas() {
        setVisible(true);

        canvasDimensions = new Vector();
    }

    public void setDirectory(EntityDirectory d) {
        directory = d;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        clear(g2d);

        for(Obstacle obstacle : directory.getObstacles()) {
            obstacle.draw(g2d, TILE_SIZE);
        }

        HashMap<Vector, Integer> slots = new HashMap<>();
        for(Player player : directory.getPlayers().values()) {
            Vector position = player.getPosition();
            if(!slots.containsKey(position)) {
                slots.put(position, 0);
            }
            int slot = slots.get(position);
            player.draw(g2d, TILE_SIZE, slot);
            slots.put(position, slot + 1);
        }
    }

    private void clear(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, canvasDimensions.X(), canvasDimensions.Y());

        for(int x = 0; x <= directory.high().X(); x ++) {
            int tileX = x * TILE_SIZE;
            g.setColor(Color.GRAY);
            g.drawLine(tileX, 0, tileX, canvasDimensions.Y());
        }

        for(int y = 0; y <= directory.high().Y(); y ++) {
            int tileY = y * TILE_SIZE;
            g.setColor(Color.GRAY);
            g.drawLine(0, tileY, canvasDimensions.X(), tileY);
        }
    }

    public void init() {
        int width = (directory.high().X() + 1) * TILE_SIZE;
        int height = (directory.high().Y() + 1) * TILE_SIZE;
        canvasDimensions.set(width, height);
        setPreferredSize(new Dimension(width, height));
        // load all of the players and obstacles as graphics
    }

    public void update() {
        // update all palyer positions
    }
}
