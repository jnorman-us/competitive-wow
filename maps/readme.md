# Map Files
Map files should be formatted as `.txt` files. The contents 
should be an `n`x`m` blob of characters. There are 4 recognized
characters mapping to an obstacle that will be generated in game.

- `. (period)` - empty space
- `w` - Wumpus
- `g` - Gold treasure
- `p` - Pit

### Sample
A sample of a valid `map.txt`. 

    ...........g...
    ..g...pg...pw..
    .pg....g.......
    ....gp.........
    wp.w...g.......
    .g.w....w......
    .p..p.p..wp....
    ....w.p......w.

There are 8 gold treasures, 8 Wumpus, and 10 pits.