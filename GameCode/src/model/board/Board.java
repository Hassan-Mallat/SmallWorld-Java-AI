/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.board;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;

import model.game.Elfs;
import model.game.Humans;
import model.game.Player;
import model.game.Unit;


public class Board extends Observable {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;


    private HashMap<Tile, Point> map = new  HashMap<Tile, Point>(); // allows retrieving the position of a tile from its reference
    private Tile[][] tileGrid = new Tile[SIZE_X][SIZE_Y]; // allows retrieving a tile from its coordinates

    // Default constructor for Board
    public Board() {
        initEmptyBoard();
    }

    public Tile[][] getTile() {
        return tileGrid;
    }
    
    // Initialize the board.
    // TODO : Distribute the biomes evenly.
    private void initEmptyBoard() {
    	
    	int indexPlains = 0;
    	int indexForest = 0;
    	int indexMountain = 0;
    	int indexDesert = 0;

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                tileGrid[x][y] = new Tile(this);
                tileGrid[x][y].biome = new Biome((int)(Math.random()*(4)+1));
                if (tileGrid[x][y].biome.getBiomeType() == 1) {
                	indexPlains++;
                } else
                if (tileGrid[x][y].biome.getBiomeType() == 2) {
                	indexForest++;
                } else
                if (tileGrid[x][y].biome.getBiomeType() == 3) {
                	indexMountain++;
                } else
                if (tileGrid[x][y].biome.getBiomeType() == 4) {
                	indexDesert++;
                }
               
                map.put(tileGrid[x][y], new Point(x, y));
            }
        }
        System.out.print("Plains Tiles : ");
        System.out.println(indexPlains);
        System.out.print("Forest Tiles : ");
        System.out.println(indexForest);
        System.out.print("Mountain Tiles : ");
        System.out.println(indexMountain);
        System.out.print("Desert Tiles : ");
        System.out.println(indexDesert);
    }
    
    private void checkTileCount(int index1, int index2, int index3, int index4, int x, int y) {
    	if (index1 > (SIZE_X * SIZE_Y) / 4) {
    		tileGrid[x][y].biome.setBiomeType((int)(Math.random()*(4)+1));
    	}
    }
    
    // Start of the game: place Units on the board.
    public void init(Player p1, Player p2) {
    	
        int unitsPerPlayer = 4; // 4v4

        /* 
        Humans human = new Humans(this, p1);
        human.goToTile(tileGrid[0][0]);

        Elfs elf = new Elfs(this, p2);
        elf.goToTile(tileGrid[SIZE_X-1][SIZE_Y-1]);
         */

        // spawning units randomly, if i want them to sticks together i can keep the commented code
        // Spawn player 1 (himans)
        for (int i = 0; i < unitsPerPlayer; i++) {
            spawnRandomUnit(p1, 1); // 1 = Human type
        }

        // Spawn player 2 (Elfs)
        for (int i = 0; i < unitsPerPlayer; i++) {
            spawnRandomUnit(p2, 2); // 2 = Elf type
        }

        setChanged();
        notifyObservers();

    }

    private void spawnRandomUnit(Player p, int type) { // i thought random spawning would turn the game more fun

        int x, y;

        do {
            x = (int)(Math.random() * SIZE_X);
            y = (int)(Math.random() * SIZE_Y);
        }
        while (tileGrid[x][y].getUnits() != null); // Keep looking if tile is occupied

        // Create the unit based on type

        Unit u;
        
        if (type == 1) u = new Humans(this, p);
        else u = new Elfs(this, p);

        // we can add logic here to spawn Dwarfs/Goblins if we want bigger game for later 

        u.goToTile(tileGrid[x][y]);
    }
    
    // Movement of a Unit on the board.
    public void reachTile(Tile tile, Unit unit) {

        tile.unit = unit;
        setChanged();
        notifyObservers();
    }

    // Use a starting Point and a range to return a list of end Points.
    public Point [] finishTileListing(Point pointStart, int range) {
    	// TODO : Fix NullPointer.
    	Point finishList [] = new Point [(2 * (range * range) + 2 * range)];
    	int finishIndex = 0;
    	Point test = new Point();
    	// Starting Point is located in the center of a diamond-shaped area, which represents the possible end Points.
    	// For each possible end Points, we check if it's within the game grid.
//    	System.out.println("Starting Point : " + pointStart);
    	for (int x = 0; x < range + 1 ; x++) {
	    	for (int y = 1; y < range + 1 - x; y++) {
	    		// Spin !
	    		test.x = pointStart.x + x;
	    		test.y = pointStart.y + y;
//	    		System.out.println("test = (" + test.x + ", " + test.y + ")");
	    		if (test.x < SIZE_X && test.y < SIZE_Y) {
	    			// North-East side. Ok.
	    			finishList[finishIndex] = new Point(test.x, test.y);
//	    			System.out.println("test ok");
	    			finishIndex++;
	    		}
	    		test.x = pointStart.x + y;
	    		test.y = pointStart.y - x;
//	    		System.out.println("test = (" + test.x + ", " + test.y + ")");
	    		if (test.x < SIZE_X && test.y >= 0) {
	    			// South-East side. Ok.
	          		finishList[finishIndex] = new Point(test.x, test.y);
//  				System.out.println("test ok");
	    			finishIndex++;
	    		}
	    		test.x = pointStart.x - y;
	    		test.y = pointStart.y + x;
//	    		System.out.println("test = (" + test.x + ", " + test.y + ")");
	    		if (test.x >= 0 && test.y < SIZE_Y) {
	    			//North-West side. Not OK.
	    			finishList[finishIndex] = new Point(test.x, test.y);
//	    			System.out.println("test ok");
	    			finishIndex++;
	    		}
	    		test.x = pointStart.x - x;
	    		test.y = pointStart.y - y;
//	    		System.out.println("test = (" + test.x + ", " + test.y + ")");
	    		if (test.x>= 0 && test.y >= 0) {
	    			// South-West side. Not OK.
	           		finishList[finishIndex] = new Point(test.x, test.y);
//	    			System.out.println("test ok");
	    			finishIndex++;
	    		}
	    	}
    	}
    	System.out.println("Returning movement preview");
    	System.out.println((finishIndex) + " deplacements possibles"); 
    	return finishList;
    }

    
    // Selection of a Unit's destination on the board
    public void moveTheUnit(Tile tile1, Tile tile2) {
        if (tile1.unit != null) {

            tile1.unit.goToTile(tile2);

        }



        setChanged();
        notifyObservers();

    }

    // Returns true if p is within the grid
    private boolean isInGrid(Point pt) {
        return pt.x >= 0 && pt.x < SIZE_X && pt.y >= 0 && pt.y < SIZE_Y;
    }
    
    // Returns the Tile at the given position
    private Tile getTilePosition(Point pt) {
        Tile retour = null;
        
        if (isInGrid(pt)) {
            retour = tileGrid[pt.x][pt.y];
        }
        return retour;
    }
    
    public Point getPosition(Tile t) {
        return map.get(t);
    }
}
