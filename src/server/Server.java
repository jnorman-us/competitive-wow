package server;

import game.Publisher;
import messages.*;
import types.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements Runnable, Publisher {
    private Receiver receiver;

    private AtomicBoolean running;
    private Thread thread;
    private ServerSocket serverChannel;

    private HashMap<String, User> directory;
    private HashSet<Client> headlessClients;
    private HashMap<User, Client> clients;

    private ReentrantLock lock;
    private Condition allConnectedCondition;

    public Server(ArrayList<User> users, int port) throws IOException {
        directory = new HashMap<>();
        for(User user : users) {
            directory.put(user.getUsername(), user);
        }
        headlessClients = new HashSet<>();
        clients = new HashMap<>();

        serverChannel = new ServerSocket(port);

        lock = new ReentrantLock();
        allConnectedCondition = lock.newCondition();

        running = new AtomicBoolean(false);
        thread = new Thread(this);
        start();
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void start() {
        running.set(true);
        thread.start();
    }

    public void run() {
        while(running.get()) {
            try {
                Socket socket = serverChannel.accept();
                Client client = new Client(this, socket);
                lock.lock();
                try {
                    headlessClients.add(client);
                } finally {
                    lock.unlock();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running.set(false);
        lock.lock();
        try {
            serverChannel.close();
            for(Client client : headlessClients) {
                client.stop();
            }
            headlessClients.clear();
            for(Client client : clients.values()) {
                client.stop();
            }
            clients.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public User attachToUser(Client headless, Login data) {
        lock.lock();
        User actual = null;
        try {
            String username = data.getUsername();
            String password = data.getPassword();
            if(directory.containsKey(username)) {
                actual = directory.get(username);
                if(actual.checkPassword(password)) {
                    headlessClients.remove(headless);
                    clients.put(actual, headless);
                    System.out.println(username + " has connected!");
                }
            } else {
                headless.stop();
                headlessClients.remove(headless);
            }
        } finally {
            allConnectedCondition.signalAll();
            lock.unlock();
        }
        return actual;
    }

    public void waitAllConnected() throws InterruptedException {
        lock.lock();
        try {
            while(clients.size() < directory.size()) {
                allConnectedCondition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void detachClient(Client client, User user) {
        lock.lock();
        try {
            if(user != null) {
                receiver.userDisconnected(user);
                clients.remove(user);
                System.out.println(user.getUsername() + " has disconnected!");
            } else {
                headlessClients.remove(client);
            }
            client.stop();
        } finally {
            allConnectedCondition.signal();
            lock.unlock();
        }
    }

    public void receiveMessage(User user, Message message) {
        if(message.type.equals(Decision.TYPE)) {
            Decision decision = (Decision) message;
            receiver.receiveDecision(user, decision);
        }
    }

    @Override
    public boolean connected(User user) {
        return clients.containsKey(user);
    }

    @Override
    public void sendMessage(User user, Message message) {
        if(connected(user)) {
            clients.get(user).send(message);
        }
    }
}
