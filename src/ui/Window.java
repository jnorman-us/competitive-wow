package ui;

import game.PlayerStatusListener;
import server.FlowListener;
import server.OnlineListener;
import types.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Window implements PlayerStatusListener, FlowListener, OnlineListener {
    private EntityDirectory directory;

    private Scoreboard scoreboard;
    private Canvas canvas;

    private JFrame scoreboardWindow;
    private JFrame canvasWindow;

    public Window(List<User> users) {
        scoreboard = new Scoreboard(users);
        canvas = new Canvas();
    }

    public void showScoreboard() {
        JFrame window = new JFrame();
        window.setTitle("Competitive WoW - Scoreboard");
        window.add(scoreboard);
        window.setVisible(true);
        window.pack();

        scoreboardWindow = window;
    }

    public void showCanvas() {
        JFrame window = new JFrame();
        window.setTitle("Competitive WoW - Map");
        window.add(canvas);
        window.setVisible(true);
        window.pack();

        canvasWindow = window;
    }

    public void setDirectory(EntityDirectory directory) {
        canvas.setDirectory(directory);
    }

    @Override
    public void roundStart() {
        canvas.update();
        canvas.repaint();
    }

    @Override
    public void roundFinish() {
        scoreboard.updateAll(UserCols.RESPONDED, BooleanCol.FALSE);
        canvas.repaint();
    }

    @Override
    public void matchStart() {
        canvas.init();
        canvasWindow.pack();
    }

    @Override
    public void matchFinish() {
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
