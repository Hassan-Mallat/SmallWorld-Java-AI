package model.game;

public class HumanPlayer extends Player {
    
    public HumanPlayer(Game _game, String _name){
        super(_game, _name);
    }

    @Override

    public Play getPlay(){

        game.receivedPlay = null;
        synchronized (game) {
            while (game.receivedPlay == null){
                try {
                game.wait();
                }
                catch (InterruptedException e) {
                e.printStackTrace();
            }
            }
        }
        return game.receivedPlay;
    }
}
