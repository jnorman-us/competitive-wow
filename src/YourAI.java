import ai.ClientAI;
import types.Direction;
import types.Vector;

import java.io.IOException;

public class YourAI extends ClientAI {
    public YourAI(String hostname, int port, String username, String password) throws IOException {
        super(hostname, port, username, password);
    }

    @Override
    public void start() {

    }

    @Override
    public Direction think() {
        return Direction.UP;
    }

    @Override
    public void died() {

    }

    @Override
    public void goldAcquired(Vector position) {

    }

    public static void main(String args[]) {
        YourAI main = null;
        try {
            main = new YourAI("127.0.0.1", 9090, "Wine_Craft", "1234");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
