package ai;

import client.TCPWorker;
import messages.*;
import robot.Robot;
import client.Receiver;
import types.Bounds;
import types.Direction;
import types.Vector;

import java.io.IOException;

public abstract class ClientAI extends Robot implements Receiver {
    private TCPWorker worker;

    public ClientAI(String hostname, int port, String username, String password) throws IOException {
        super(null, new Vector());

        worker = new TCPWorker(hostname, port, new Login(username, password), this);
    }

    public abstract void start();
    public abstract Direction think();
    public abstract void died();
    public abstract void goldAcquired(Vector position);

    @Override
    public void receiveInitWorld(InitWorld message) {
        bounds = message;
        System.out.println("World Initialized to " + bounds.high());
        start();
    }

    @Override
    public void receiveRequestThink(RequestThink message) {
        position.copy(message.currentPosition);
        smelly = message.smelly;
        windy = message.windy;
        shiny = message.shiny;

        Direction toMove = think();
        worker.send(new Decision(toMove));
    }

    @Override
    public void receiveAcquiredGold(AcquiredGold message) {
        goldAcquired(message.currentPosition);
    }

    @Override
    public void receiveDied(Died message) {
        died();
    }

    public Vector getPosition() {
        return position;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(position);
        sb.append("\n");
        sb.append("Shiny, Smelly, Windy:\n");
        sb.append(shiny);
        sb.append(", ");
        sb.append(smelly);
        sb.append(", ");
        sb.append(windy);
        return sb.toString();
    }
}
