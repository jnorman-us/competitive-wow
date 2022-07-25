package server;

import types.User;

public interface OnlineListener {
    void userConnected(User user);
    void userDisconnected(User user);
}
