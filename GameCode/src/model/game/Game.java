package model.game;

import model.board.Board;
import model.board.Tile;

public class Game extends Thread{

    private Board board;
    private Player player1;
    private Player player2;
    protected Play receivedPlay;


    // Default constructor for Game
    public Game() {

        board = new Board();
        player1 = new HumanPlayer(this, "Player 1"); // human player
        player2 = new AIPlayer(this, "Computer"); // ai player

        board.init(player1, player2);
        start();
    }

    public Board getBoard() {
        return board;
    }

    // Notification that a Play has been made
    public void sendPlay(Play play) {
        receivedPlay = play;

        synchronized (this) {
            notify();
        }
    }

    public void resolvePlay(Player currentPlayer, Play play) {


        if (play == null || play.start == null || play.target == null) return;
        
        Unit sourceUnit = play.start.getUnits();

        if (sourceUnit == null || sourceUnit.getOwner() != currentPlayer) return;


        // validate that target is within movement range (use Board.finishTileListing)
        java.awt.Point startPos = board.getPosition(play.start);
        java.awt.Point targetPos = board.getPosition(play.target);
        if (startPos == null || targetPos == null) {
            System.out.println("Invalid Move: Unknown tile position");
            return;
        }
        int moveRange = sourceUnit.getMovementRange();
        java.awt.Point[] reachable = board.finishTileListing(startPos, moveRange);
        boolean inRange = false;
        if (reachable != null) {
            for (java.awt.Point p : reachable) {
                if (p == null) continue;
                if (p.x == targetPos.x && p.y == targetPos.y) {
                    inRange = true;
                    break;
                }
            }
        }
        // allow staying on the same tile (or treat as invalid if you prefer)
        if (!inRange && !(startPos.x == targetPos.x && startPos.y == targetPos.y)) {
            System.out.println("Invalid Move: Tiles are too far");
            return;
        }


        Unit targetUnit = play.target.getUnits();
        
        int attackPower = sourceUnit.getQuantity(); // useful to do 2v1 3v3 2v2 etc


        // 1) movement logic
        // "attacker" is the single unit moving/attacking
        
        Unit movingUnit; 
        boolean isSplit = false; // we will be spliting the unit in case of quantity > 1

        if (sourceUnit.getQuantity() > 1) {

            // splitting the Stack
            isSplit = true;
            sourceUnit.setQuantity(sourceUnit.getQuantity() - 1); // Leave others behind
            
            // Creating the clone
            movingUnit = sourceUnit.copy(); // it's like our big Unite got split into clones
            movingUnit.setQuantity(1); // It travels alone
        }
        
        else { // case : Last Unit Moving  
            movingUnit = sourceUnit;
        }


        // 2) interaction logic (3 cases : empty or friend or enemy)
        
        if (targetUnit == null) {

            // A - Move to empty tile
            if (isSplit) {
                // We manually place the clone (Board.moveTheUnit won't work for clones usually)
                board.reachTile(play.target, movingUnit); 
            }    
            else {
                // Standard move
                board.moveTheUnit(play.start, play.target);
            }
            System.out.println("Unit moved to empty tile."); //test


        }
        
        else if (targetUnit.getOwner() == currentPlayer) { // B - Merge with its friend
            
            // +1 quantity
            targetUnit.setQuantity(targetUnit.getQuantity() + 1);
            
            // If it wasn't a split (ie, we moved the last unit), we must remove the empty case
            if (!isSplit) {
                play.start.exitingTile();
            }
            System.out.println("Merged 1 unit into friendly stack."); //test

        }
        
        else {  // C - Combat (Enemy)
                // check if there's a bonus for stack size
                // attackPower vs defenderStack
 
            
            
            
            int defensePower = targetUnit.getQuantity();
            boolean attackerWins = CombatManager.resolveAttack(attackPower, defensePower);

            //System.out.println("Combat: " + attackPower + " Attacker vs " + targetUnit.getQuantity() + " Defenders");

            if (targetUnit.getFavoredBiomeId() == play.target.getBiome().getBiomeType()) {

                System.out.println("Defender has Biome Advantage! (+1 Dice Power)");
                defensePower++; 
            }


            if (attackerWins) {

                System.out.println("Attacker Won!");
                
                // only 1 defender dies in each battle
                if (targetUnit.getQuantity() > 1) {
                    targetUnit.setQuantity(targetUnit.getQuantity() - 1);
                    System.out.println("Defender lost 1 unit. Stack remains.");
                    
                    // if the attack succeeded (we killed someone), but the tile isn't empty. exmpl : 3 v 3 after winning attak will be 3 v 2
                    // The moving unit cannot enter yet because we cant have friends and enemies un the same place. so it "bounces back"
                    // If it was a split (clone), it disappears
                    // If it was the last unit, it stays on start

                    if (isSplit) {
                       // The clone returns to the main stack (undoing the split)
                       sourceUnit.setQuantity(sourceUnit.getQuantity() + 1);
                    }
                    // If not a isSplit, it just stays where it is
                    
                }

                else {
                    // Defender wiped out (everyone dead)
                    play.target.exitingTile(); 
                    
                    // now the attacker can move in
                    if (isSplit) board.reachTile(play.target, movingUnit);
                    else board.moveTheUnit(play.start, play.target);
                    
                    // Award Point
                    currentPlayer.addScore(1); 
                }

            }
            
            else {
                System.out.println("Attacker Lost.");
                // Attacker dies. 
                if (!isSplit) play.start.exitingTile();
                // If isSplit, the clone dies automatically (we don't add it back to source). -1 attacker auto
            }
        }
    }


    
    private boolean isAdjacent(Tile t1, Tile t2) {

        // Get coordinates from the board
        java.awt.Point p1 = board.getPosition(t1);
        java.awt.Point p2 = board.getPosition(t2);

        if (p1 == null || p2 == null) return false;

        // Calculate distance
        int dx = Math.abs(p1.x - p2.x);
        int dy = Math.abs(p1.y - p2.y);

        // They are adjacent if sum of differences is exactly 1
        // (x changed by 1 AND y changed by 0) OR (x changed by 0 AND y changed by 1)
        return (dx + dy) == 1;
    }
    
    
    // Start the Game
    public void run() {
        playGame();
    }
    
    // Game loop.
    public void playGame() {
        int maxTurns = 6;

        /*
        while(true) {

            // Player 1: 
            System.out.println("Turn: " + player1.getName());
            Play play1 = player1.getPlay();
            resolvePlay(play1);

            // Player 2:
            System.out.println("Turn: " + player2.getName());
            Play play2 = player2.getPlay();
            resolvePlay(play2);
        }*/

            for (int turn = 1; turn <= maxTurns; turn++) {
                System.out.println("--- TURN " + turn + " / " + maxTurns + " ---");


                // Player1 's turn

                System.out.println(player1.getName() + "'s turn (Score: " + player1.getScore() + ")");
                Play play1 = player1.getPlay();

                resolvePlay(player1, play1);
                calculateEndTurnScore(player1); // Points for holding territory

                // Player's 2 turn
                System.out.println(player2.getName() + "'s turn (Score: " + player2.getScore() + ")");
                Play play2 = player2.getPlay();

                resolvePlay(player2, play2);
                calculateEndTurnScore(player2);
        }


        // Game Over, who won???
        System.out.println("--- GAME OVER ---");
        System.out.println(player1.getName() + ": " + player1.getScore());
        System.out.println(player2.getName() + ": " + player2.getScore());

        if (player1.getScore() > player2.getScore()) {
            System.out.println("WINNER: " + player1.getName() );
        }
        else if (player2.getScore() > player1.getScore()) {
            System.out.println("WINNER: " + player2.getName());
        }
        else {
            System.out.println("It's a DRAW!");
        }
    }



    // Calculate points for holding territory
    private void calculateEndTurnScore(Player p) {

        Tile[][] grid = board.getTile();

        for (int x = 0; x < Board.SIZE_X; x++) {
            for (int y = 0; y < Board.SIZE_Y; y++) {

                Unit u = grid[x][y].getUnits();

                // 1 point for every tile occupied by your unit 
                // if the tile matches the unit's favorite biome

                /* old rule i didnt like 
                if (u != null && u.getOwner() == p) {
                    if (u.getFavoredBiomeId() == grid[x][y].getBiome().getBiomeType()) {
                        p.addScore(1);
                    }
                }
                 */

                if (u != null && u.getOwner() == p) {
                    p.addScore(1); 
                }
            }
        }
    }
}
