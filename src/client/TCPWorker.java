package client;

import messages.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPWorker implements Runnable {
    private Receiver receiver;
    private Login credentials;

    private AtomicBoolean running;
    private Thread thread;

    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public TCPWorker(String hostname, int port, Login credentials, Receiver receiver) throws IOException {
        this.receiver = receiver;
        this.credentials = credentials;

        running = new AtomicBoolean(false);
        thread = new Thread(this);

        socket = new Socket(hostname, port);
        output = socket.getOutputStream();
        input = socket.getInputStream();
        writer = new ObjectOutputStream(output);
        reader = new ObjectInputStream(input);

        start();
        send(credentials); // login
    }

    public void start() {
        running.set(true);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while(running.get()) {
                Message message = (Message) reader.readObject();
                if(message == null) break;

                if(message.type.equals(InitWorld.TYPE)) {
                    receiver.receiveInitWorld((InitWorld) message);
                } else if(message.type.equals(RequestThink.TYPE)) {
                    receiver.receiveRequestThink((RequestThink) message);
                } else if(message.type.equals(AcquiredGold.TYPE)) {
                    receiver.receiveAcquiredGold((AcquiredGold) message);
                } else if(message.type.equals(Died.TYPE)) {
                    receiver.receiveDied((Died) message);
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(running.get()) {
                running.set(false);
            }
        }
    }

    public void send(Message message) {
        try {
            writer.writeObject(message);
        } catch(IOException e) {
            if(running.get()) {
                running.set(false);
            }
            e.printStackTrace();
        }
    }

    public void stop() {
        running.set(false);
        try {
            this.input.close();
            this.reader.close();
            this.socket.close();
        } catch(IOException e) {

        }
    }
}
