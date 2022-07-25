package game;

import obstacles.Gold;
import obstacles.Monster;
import obstacles.Pit;
import types.Vector;

import java.io.*;
import java.util.ArrayList;

public class WorldMap {
    public int maxWidth, maxHeight;
    public ArrayList<Pit> pits;
    public ArrayList<Gold> golds;
    public ArrayList<Monster> monsters;

    public WorldMap(File mapFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(mapFile));

        pits = new ArrayList<>();
        golds = new ArrayList<>();
        monsters = new ArrayList<>();

        String line = reader.readLine();
        int x = 0, y = 0;
        while(line != null) {
            x = 0;
            for(char c : line.toCharArray()) {
                switch(c) {
                    case 'p':
                        pits.add(new Pit(new Vector(x, y)));
                        break;
                    case 'g':
                        golds.add(new Gold(new Vector(x, y)));
                        break;
                    case 'w':
                        monsters.add(new Monster(new Vector(x, y)));
                        break;
                    default:
                        break;
                }
                x ++;
            }
            line = reader.readLine();
            y ++;
        }
        maxWidth = x - 1;
        maxHeight = y - 1;
    }
}
