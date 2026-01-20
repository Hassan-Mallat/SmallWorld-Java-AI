package model.game;

import model.board.Tile;

public class Play {
    protected Tile start;
    protected Tile target;
    
    // Constructor for Play with a start Tile and a target Tile.
    public Play(Tile _start, Tile _target) {
        start = _start;
        target = _target;
    }
}
