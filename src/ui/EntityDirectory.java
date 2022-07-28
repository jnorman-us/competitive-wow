package ui;

import game.Player;
import obstacles.Obstacle;
import types.Bounds;
import types.User;

import java.util.Map;
import java.util.List;

public interface EntityDirectory extends Bounds {
    void startDraw();
    void endDraw();
    List<Obstacle> getObstacles();
    Map<User, Player> getPlayers();
}
