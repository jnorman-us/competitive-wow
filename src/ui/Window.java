package ui;

import game.PlayerStatusListener;
import server.FlowListener;
import server.OnlineListener;
import types.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Window extends JFrame implements PlayerStatusListener, FlowListener, OnlineListener {
    private EntityDirectory directory;

    private Scoreboard scoreboard;

    public Window(List<User> users) {
        scoreboard = new Scoreboard(users);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Scrollable JTable");
        setContentPane(scoreboard);
        pack();
        setVisible(true);
    }

    public void setDirectory(EntityDirectory directory) {
        this.directory = directory;
    }

    @Override
    public void roundComplete() {
        scoreboard.updateAll(UserCols.RESPONDED, BooleanCol.FALSE);
    }

    @Override
    public void matchComplete() {
        scoreboard.updateAll(UserCols.DEAD, BooleanCol.FALSE);
        scoreboard.updateAll(UserCols.GOLD, "" + 0);
    }

    @Override
    public void playerDied(User user) {
        scoreboard.updateUser(user, UserCols.DEAD, BooleanCol.TRUE);
    }

    @Override
    public void playerResponded(User user) {
        scoreboard.updateUser(user, UserCols.RESPONDED, BooleanCol.TRUE);
    }

    @Override
    public void playerScoreUpdate(User user, int score) {
        scoreboard.updateUser(user, UserCols.SCORE, "" + score);
    }

    @Override
    public void playerGoldUpdate(User user, int collected) {
        scoreboard.updateUser(user, UserCols.GOLD, "" + collected);
    }

    @Override
    public void userConnected(User user) {
        scoreboard.updateUser(user, UserCols.ONLINE, BooleanCol.TRUE);
    }

    @Override
    public void userDisconnected(User user) {
        scoreboard.updateUser(user, UserCols.ONLINE, BooleanCol.FALSE);
    }
}
