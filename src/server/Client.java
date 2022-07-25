package server;

import messages.Decision;
import messages.Login;
import messages.Message;
import types.User;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements Runnable {
    private Server server;
    private User user;

    private AtomicBoolean running;
    private Thread thread;

    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public Client(Server server, Socket socket) throws IOException {
        this.server = server;

        this.socket = socket;
        input = socket.getInputStream();
        output = socket.getOutputStream();
        reader = new ObjectInputStream(input);
        writer = new ObjectOutputStream(output);

        running = new AtomicBoolean(false);
        thread = new Thread(this);
        start();
    }

    public void start() {
        running.set(true);
        thread.start();
    }

    public void run() {
        try {
            while(running.get()) {
                Message message = (Message) reader.readObject();
                if(message == null) break;

                if(message.type.equals(Login.TYPE)) {
                    Login data = (Login) message;
                    user = server.attachToUser(this, data);
                } else if(user != null) {
                    server.receiveMessage(user, message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(running.get()) {
                running.set(false);
                server.detachClient(this, user);
            }
        }
    }

    public void send(Message message) {
        try {
            System.out.println(message);
            writer.writeObject(message);
        } catch(IOException e) {
            if(running.get()) {
                running.set(false);
                server.detachClient(this, user);
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
