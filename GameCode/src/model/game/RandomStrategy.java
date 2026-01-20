package model.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.board.Board;
import model.board.Tile;


// This will be our "easy mode" ai will move/play randombly not based on real calculations
public class RandomStrategy implements AIStrategy {

    @Override

    public Play calculateMove(Player me, Board board) {

        Tile[][] grid = board.getTile();
        List<Unit> myUnits = new ArrayList<>();

        // Find all my units (getOwner == me)
        for (int x = 0; x < Board.SIZE_X; x++) {
            for (int y = 0; y < Board.SIZE_Y; y++) {
                Unit u = grid[x][y].getUnits();
                if (u != null && u.getOwner() == me) {
                    myUnits.add(u);
                }
            }
        }

        if (myUnits.isEmpty()) return null; // No units left

        // Shuffle units to pick one at random (no thinking)
        Random rand = new Random();
        

        // Try up to 10 times to find a unit that has a valid move, 10 is enough

        for (int i = 0; i < 10; i++) {

            Unit rUnit = myUnits.get(rand.nextInt(myUnits.size()));
            Point pos = board.getPosition(rUnit.getTile());
            
            if (pos != null) {

                // Pick a random direction of our 4 neighbours

                int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
                int[] dir = directions[rand.nextInt(4)];
                
                int nx = pos.x + dir[0];
                int ny = pos.y + dir[1];

                // Check if target is on the map
                
                if (nx >= 0 && nx < Board.SIZE_X && ny >= 0 && ny < Board.SIZE_Y) {
                    Tile target = grid[nx][ny];
                    

                    if (target != null) {
                        System.out.println("RandomStrategy: Picking a random move!");
                        return new Play(rUnit.getTile(), target);
                    }   
                }
            }
        }

        return null; // No valid random move found...
    }
}