package server;

import game.Game;
import game.Publisher;
import game.WorldMap;
import types.User;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if(args.length != 3) return;
        int port = Integer.parseInt(args[0]);
        String mapsDirectoryPath = args[1];
        String usersFilePath = args[2];

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

                while(!game.finished()) {
                    game.playRound();
                }
            } catch (IOException e) {
                System.out.println("Failed to read world file");
                e.printStackTrace();
                return;
            }
        }
    }
}