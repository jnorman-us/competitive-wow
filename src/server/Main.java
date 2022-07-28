package server;

import game.Game;
import game.Publisher;
import game.WorldMap;
import types.User;
import ui.Window;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if(args.length != 4) return;
        int port = Integer.parseInt(args[0]);
        int tileSize = Integer.parseInt(args[1]);
        String mapsDirectoryPath = args[2];
        String usersFilePath = args[3];

        // LOAD USERS FROM FILE
        ArrayList<User> players = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                new FileReader(usersFilePath)
            );
            String line = reader.readLine();
            while(line != null) {
                String[] splitRecord = line.split(",");
                players.add(new User(splitRecord[0], splitRecord[1]));
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Failed to load users!");
            e.printStackTrace();
            return;
        }

        Window window = new Window(players, tileSize);

        // INITIALIZE GAME
        Server server = null;
        try {
            server = new Server(players, port);
        } catch (IOException e) {
            System.out.println("Failed to start server!");
            e.printStackTrace();
            return;
        }
        Game game = new Game(players);
        server.setReceiver(game);
        game.setPublisher(server);

        window.setDirectory(game);
        server.setOnlineListener(window);
        game.setStatusListener(window);
        window.showScoreboard();
        window.showCanvas();

        // FOR EACH WORLD MAP, PLAY THE GAME
        File mapDirectory = new File(mapsDirectoryPath);
        File[] mapFiles = mapDirectory.listFiles();
        for(File mapFile : mapFiles) {
            // WAIT FOR ALL PLAYERS TO CONNECT
            try {
                System.out.println("Waiting for all connected...");
                server.waitAllConnected();
                System.out.println("All connected!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("Starting world " + mapFile);
                WorldMap world = new WorldMap(mapFile);
                game.initFromMap(world);
                window.matchStart();

                Thread.sleep(1000);

                while(!game.finished()) {
                    window.roundStart();
                    game.playRound();
                    Thread.sleep(500);
                    window.roundFinish();
                }
                Thread.sleep(1000);
                window.matchFinish();
            } catch (IOException e) {
                System.out.println("Failed to read world file");
                e.printStackTrace();
                return;
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
