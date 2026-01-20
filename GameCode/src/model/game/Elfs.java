package model.game;

import model.board.Board;

public class Elfs extends Unit {
   
	public Elfs(Board _board, Player _owner) {
        super(_board, _owner);
    }

    @Override
    public int getFavoredBiomeId(){
        return 2; //they love the forest
    }

    @Override
    public Unit copy(){
        return new Elfs(this.board, this.owner); // creating new elf in the same board that has the same owner
    }
}
