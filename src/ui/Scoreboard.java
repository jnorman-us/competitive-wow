package ui;

import types.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Scoreboard extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private HashMap<User, Integer> userRows;

    public Scoreboard(List<User> users) {
        model = new DefaultTableModel(new String[][]{}, new String[]{
                "Online", "Username", "Responded", "Dead", "Gold", "Score",
        });
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        userRows = new HashMap<>();
        for(User user : users) {
            model.addRow(new String[]{
                    BooleanCol.FALSE, user.getUsername(), BooleanCol.FALSE, BooleanCol.FALSE, "0", "0"
            });
            userRows.put(user, model.getRowCount() - 1);
        }


        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        setSize(800, 500);
        setOpaque(true);
        add(scrollPane);
    }

    public void updateAll(int col, String value) {
        for(int row : userRows.values()) {
            model.setValueAt(value, row, col);
        }
    }

    public void updateUser(User user, int col, String value) {
        if(userRows.containsKey(user)) {
            int row = userRows.get(user);
            model.setValueAt(value, row, col);
        }
    }
}
