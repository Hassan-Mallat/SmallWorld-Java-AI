package model.board;

import model.game.Unit;

public class Biome {
	private int biome_id;
	
	public Biome (int _biome_id) {
		biome_id = _biome_id;
	}
	
	public int getBiomeType () {
		return biome_id;
	}
	
	public void setBiomeType(int _biome_id) {
		biome_id = _biome_id;
	}
	
	public void EnhanceUnit (Unit unit) {
		if (biome_id == unit.getTile().getBiome().getBiomeType()) {
			// TODO : Enhance this unit
		}
	}
}
