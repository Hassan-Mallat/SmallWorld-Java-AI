/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.board;

import model.game.Unit;

public class Tile {

    protected Biome biome;
    protected Unit unit;
    protected Board board;


    // A Unit leaves this Tile.
    public void exitingTile() {
        unit = null;
    }


    // Tile constructor with the Board
    public Tile(Board _board) {

        board = _board;
    }

    public Unit getUnits() {
        return unit;
    }

    public Biome getBiome() {
    	return biome;
    }
}
