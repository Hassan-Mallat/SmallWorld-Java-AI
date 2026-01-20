package model.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.board.Board;
import model.board.Tile;

public class SmartStrategy implements AIStrategy {

    @Override
    public Play calculateMove(Player me, Board board) {

        Tile[][] grid = board.getTile();
        Play bestMove = null; // best move not found yet
        int bestScore = 0; // or we can "Integer.MIN_VALUE; " if we want to have default score as the minimum number in java


        // Finding all my units

        List<Unit> myUnits = new ArrayList<>();

        for (int x = 0; x < Board.SIZE_X; x++) {
            for (int y = 0; y < Board.SIZE_Y; y++) {
                Unit u = grid[x][y].getUnits();
                if (u != null && u.getOwner() == me) {
                    myUnits.add(u); // store all my units in a list
                }
            }
        }


        // Evaluate moves for each unit

        for (Unit unit : myUnits) {

            Point currentPos = findPosition(unit.getTile(), grid);
            if (currentPos == null) continue; // "defensive programming", 
                                              // i put it as a protection from bugs in other files

            // Check 4 neighbors (Up, Down, Left, Right)
            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            
            for (int[] dir : directions) {

                int nx = currentPos.x + dir[0];
                int ny = currentPos.y + dir[1];

                // Check bounds
                if (nx >= 0 && nx < Board.SIZE_X && ny >= 0 && ny < Board.SIZE_Y) {

                    Tile targetTile = grid[nx][ny];
                    int score = evaluateMove(unit, targetTile);

                    // If this is the best move so far, save it
                    if (score > bestScore) {

                        bestScore = score;
                        bestMove = new Play(unit.getTile(), targetTile);
                    }
                }
            }
        }

        if (bestScore <= 0) {
            System.out.println("All moves are bad! I'll skip for now...");
            return null;
        }

        System.out.println("AI player decided on the move with score: " + bestScore);
        return bestMove;
    }

    
    // Assigning a score to a move.
    // increasing and decreasing score based on risks and bonus moves
    // computation is based on 2 main factors

    private int evaluateMove(Unit me, Tile target) {

        int score = 0;
        Unit targetUnit = target.getUnits();


        // If target biome matches my preference (land etc), big bonus, proba of winning increases.
        if (target.getBiome().getBiomeType() == me.getFavoredBiomeId()) {
            score += 10; 
        }

        // factor 2 : Combat & Occupancy

        if (targetUnit == null) { // Empty tile: Good for expansion, i can move with no risk
            score += 5;
        }

        else if (targetUnit.getOwner() == me.getOwner()) { // Friendly unit, stronger together :p
            score += 15; 
        }

        else {  // Enemy unit: We have to calculate combat risk

            int myPower = me.getQuantity();
            int enemyPower = targetUnit.getQuantity();

            if (myPower > enemyPower) { // Likely to win
                score += 50; 
            }
            else if (myPower == enemyPower) { // Risky, it's a gamble (50/50) better to avoid it
                score -= 10;
            }
            else { // Likely to die (Suicide) we ain't the main character x(
                score -= 100;
            }
        }

        return score;
    }


    private Point findPosition(Tile t, Tile[][] grid) {
        for (int x = 0; x < Board.SIZE_X; x++) {
            for (int y = 0; y < Board.SIZE_Y; y++) {
                if (grid[x][y] == t) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
}