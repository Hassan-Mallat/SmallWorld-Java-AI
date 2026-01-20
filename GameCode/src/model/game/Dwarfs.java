package model.game;

import model.board.Board;

public class Dwarfs extends Unit {
	
	public Dwarfs (Board _board, Player _owner) {
		super(_board, _owner);
	}

	@Override
    public int getFavoredBiomeId(){
        return 3; //they love the forest mountains
    }

	@Override
    public Unit copy(){
        return new Dwarfs(this.board, this.owner); // creating new Dwarf in the same board that has the same owner
    }
}
