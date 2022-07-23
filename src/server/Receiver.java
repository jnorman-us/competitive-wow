package server;

import messages.Decision;
import types.User;

public interface Receiver {
    void userDisconnected(User user);
    void receiveDecision(User user, Decision message);
}
