package model.game;

import model.board.Board;

public class Humans extends Unit {
	
	public Humans (Board _board, Player _owner) {
		super(_board, _owner);
	}

	@Override
    public int getFavoredBiomeId(){
        return 1; //they love the plain
    }

	@Override
    public Unit copy(){
        return new Humans(this.board, this.owner); // creating new Humans in the same board that has the same owner
    }
}
