package game;

import messages.RequestThink;
import obstacles.Gold;
import obstacles.Monster;
import obstacles.Obstacle;
import messages.Decision;
import messages.InitWorld;
import obstacles.Pit;
import server.Receiver;
import types.Bounds;
import types.User;
import types.Vector;
import ui.EntityDirectory;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Game implements Receiver, Bounds, EntityDirectory {
    public Publisher publisher;
    public PlayerStatusListener statusListener;

    private int width, height;
    private HashMap<User, Player> players;

    private int goldInRound;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Pit> pits;
    private ArrayList<Gold> golds;
    private ArrayList<Monster> monsters;

    private ReentrantLock lock;
    private Condition roundFinishedCondition;

    public Game(ArrayList<User> users) {
        players = new HashMap<>();
        for(User user : users) {
            players.put(user, new Player(this, user, new Vector()));
        }
        goldInRound = 0;
        obstacles = new ArrayList<>();
        pits = new ArrayList<>();
        golds = new ArrayList<>();
        monsters = new ArrayList<>();

        lock = new ReentrantLock();
        roundFinishedCondition = lock.newCondition();
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setStatusListener(PlayerStatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public void initFromMap(WorldMap map) {
        width = map.maxWidth;
        height = map.maxHeight;

        obstacles.clear();
        obstacles.addAll(map.pits);
        obstacles.addAll(map.golds);
        obstacles.addAll(map.monsters);

        pits.clear();
        pits.addAll(map.pits);

        golds.clear();
        golds.addAll(map.golds);

        monsters.clear();
        monsters.addAll(map.monsters);

        goldInRound = map.golds.size();

        for(User user : players.keySet()) {
            Player player = players.get(user);
            player.reset();
            publisher.sendMessage(user, new InitWorld(this));
        }
    }

    public void playRound() {
        lock.lock();
        try {
            for (User user : players.keySet()) {
                Player player = players.get(user);
                player.resetReceived();
                if(!player.isDead()) {
                    publisher.sendMessage(user, new RequestThink(
                            player.getPosition(),
                            player.isSmelly(),
                            player.isWindy(),
                            player.isShiny()
                    ));
                }
            }
            while(!roundFinished()) {
                roundFinishedCondition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public boolean roundFinished() {
        for(Player player : players.values()) {
            if(!player.hasReceived() && !player.isDead()) return false;
        }
        return true;
    }

    public boolean finished() {
        for(Player player : players.values()) {
            boolean dead = player.isDead();
            boolean finished = player.goldCollected() == goldInRound;

            if(!dead && !finished) return false;
        }
        return true;
    }

    @Override
    public void userDisconnected(User user) {
        lock.lock();
        try {
            Player player = players.get(user);
            player.kill();
        } finally {
            roundFinishedCondition.signalAll();
            lock.unlock();
        }
    }

    @Override
    public void receiveDecision(User user, Decision message) {
        lock.lock();
        try {
            Player player = players.get(user);

            if(!player.hasReceived() && !player.isDead()) {
                player.clearSensors();
                player.act(message.getAction());

                for(Obstacle obstacle : obstacles) {
                    obstacle.calculateOnTop(player);
                }
                statusListener.playerResponded(user);
            }
        } finally {
            roundFinishedCondition.signalAll();
            lock.unlock();
        }
    }

    @Override
    public Vector high() {
        return new Vector(width, height);
    }

    @Override
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    @Override
    public Map<User, Player> getPlayers() {
        return players;
    }

    public Monster getMonsterAt(Vector position) {
        for(Monster monster : monsters) {
            if(monster.getPosition().equals(position)) {
                return monster;
            }
        }
        return null;
    }
}
