package model.game;
import model.board.Board;

public interface  AIStrategy {
    Play calculateMove(Player me, Board board);
}
