package ui;

import game.Player;
import obstacles.Obstacle;
import types.User;

import java.util.Map;
import java.util.List;

public interface EntityDirectory {
    List<Obstacle> getObstacles();
    Map<User, Player> getPlayers();
}
