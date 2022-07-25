package game;

import types.User;

public interface PlayerStatusListener {
    void playerDied(User user);
    void playerResponded(User user);
    void playerScoreUpdate(User user, int score);
    void playerGoldUpdate(User user, int collected);
}
