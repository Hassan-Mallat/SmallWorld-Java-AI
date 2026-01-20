package model.game;

import model.board.Board;
import model.board.Tile;


// Entities that can move

public abstract class Unit {

    protected Tile tile;
    protected Board board;
    protected Player owner;
    protected int quantity; // x vs y it's important to calculate proba of winning etc..

    // Constructor of a Unit on a Board
    public Unit(Board _board, Player _owner) {
        this.board = _board;
        this.owner = _owner;
        this.quantity = 1; // defaukt value.
    }
    
    // we have to get favorite "terrain" of each biome
    // 1 = Plain, 2 = Forest, 3 = Mounrain, 4 = Desert
    public abstract int getFavoredBiomeId();
    
    // movement range for the unit (default implementation).
    public int getMovementRange() {
        return 4;
    }
    
    public Player getOwner() {
        return owner;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int q) {
        this.quantity = q;
    }
    // A unit leaves a Tile
    public void exitTile() {
        tile.exitingTile();
    }
    
    // A unit arrives at the Tile
    public void goToTile(Tile _tile) {
        if (tile != null) {
            exitTile();
            
        }
        tile = _tile;
        board.reachTile(tile, this);
    }

    public Tile getTile() {
        return tile;
    }


    public abstract Unit copy();



}
