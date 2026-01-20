package model.game;

import model.board.Board;

public class Goblins extends Unit {
   
	public Goblins(Board _board, Player _owner) {
        super(_board, _owner);
    }

    @Override
    public int getFavoredBiomeId(){
        return 4; //they love the desert
    }

    @Override
    public Unit copy(){
        return new Goblins(this.board, this.owner); // creating new Goblin in the same board that has the same owner
    }
}
