package game;

import messages.Died;
import messages.InitWorld;
import messages.Message;
import messages.RequestThink;
import types.User;

public interface Publisher {
    boolean connected(User user);
    void sendMessage(User user, Message message);
}
