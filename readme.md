# Competitive World of Wumpus

## What is World of Wumpus?
This project is based on the World of Wumpus, a toy game coined by early AI
researchers to put their AIs to practice. The game is set in a 2D tile map
with pits, Wumpus (a monster), and treasure. The objective is for the
playing AI to traverse the map to find treasure without running into either
the pits or Wumpus.

However, the AI is only able to observe a subset of the world through
the use of their limited sensors. When the AI is on a tile, they can
only tell:

- If there is a stench (an adjacent tile contains Wumpus)
- If there is a breeze (an adjacent tile contains a pit)
- If there is a shine (an adjacent tile contains treasure)

As the AI wanders around, they take stock of their surroundings and
attempt to guess what the world around them is like (i.e. if the tile
next to them is dangerous or not). By combining their observations with
logic, they can more confidently reach their objectives.

## Competition
This program is split up into a competition server and the client AIs.
The server is already implemented along with the client networking code.
All that is left is for a competitor to implement the logic behind their
AI.

Once each competitor is ready, they connect to the server and 
concurrently play their own games of World of Wumpus. For each map 
provided in the maps directory, the server:

1. Makes sure each competitor is connected to the TCP server
2. Informs each client the match is starting and informs them of game 
metadata such as width/height
3. Prompts each client to make a decision
   1. Provides the observable world state
   2. Waits until they respond with their movement direction
4. Stalls until all able clients respond
   1. Clients are **disabled** if:
      1. They disconnect (killed until the next match)
      2. They die to Wumpus or a pit
      3. They completed the match by collecting all golds
5. Repeats step 2-4 until all clients are **disabled** (the match
is over)

The server will repeat steps 1-5 for each provided map, then spit out
scores at the end.

## Running the Server
The server is easy to execute. Locate the `CompetitiveWoWServer.jar` 
file, then execute the following command while providing program
arguments.

    java -jar CompetitiveWoWServer.jar <port> <maps-directory> <users-file>

| Argument | Description | Sample |
| --- | --- | --- |
| `port` | port number to host the server on | `9090` |
| `maps-directory` | path to the directory containing map `.txt` files | `maps/` |
| `users-file` | path to the file with usernames,passwords | `users.csv` |

## Implementing the YourAI AI
Provided in this repository is `ai.ClientAI`, an abstract class that
hides the implementation details of networking with the server. That
leaves only a few methods to implement.

    ClientAI(String hostname, int port, String username, String password); // constructor
    
    void start();
    Direction think();
    void died();
    void goldAcquired(Vector position);

### `void start()`
`start()` is called when the new map file is loaded on the server, meaning
the start of a new match. No implementation is required, but it could be
helpful to initialize the variables you'll use for logic.

### `Direction think()`
`think()` is called when your AI needs to decide which direction it will
go in. The bulk of your logic should go inside this function. It should
return the selected `Direction`.

### `void died()`
`died()` is called when your player has walked into either a pit or Wumpus
in order to notify you of your unfortunate fate. Until the next match, the 
`think()` function will not be called, so you'll have to wait.

### `void goldAcquired(Vector position)`
`goldAcquired()` is called when your player has walked into treasure,
meaning that you're `1/8` closer to winning the game. The position Vector
is provided so you don't waste time trying to collect this gold again.

## Provided Sensor Data
Before your `think()` implementation is called, the server sends over
the details of your current tile. You can access these by calling
these methods to sense the world around you.

### `Vector getPosition()`
Returns the current position of your AI.

### `boolean isSmelly()`
Returns true if an adjacent tile contains Wumpus

### `boolean isShiny()`
Returns true if an adjacent tile contains Gold Treasure

### `boolean isWindy()`
Returns true if an adjacent tile contains a Pit

## Types
There are a few types that you should familiarize yourself with. They
come with the repository and are kept in the `types` package.

### `types.Direction`

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
   
### `types.Vector`

    public class Vector {
        public int X();
        public int Y();
    }