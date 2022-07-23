package client;

import messages.AcquiredGold;
import messages.Died;
import messages.InitWorld;
import messages.RequestThink;

public interface Receiver {
    void receiveInitWorld(InitWorld message);
    void receiveRequestThink(RequestThink message);
    void receiveDied(Died message);
    void receiveAcquiredGold(AcquiredGold message);
}
