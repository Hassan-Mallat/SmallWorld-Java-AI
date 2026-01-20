package model.game;

// we split Player class into 2 categorise human player and AI player...
public abstract  class Player {
    protected  Game game;
    protected String name;
    protected int score = 0;

    // Constructor of a Player for a Game
    public Player(Game _game, String _name) {
        this.game = _game;
        this.name = _name;
    }

    public String getName(){
        return name;
    }

    public abstract Play getPlay();

    /*
    synchronized (game) {
            try {
                game.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return game.receivedPlay;
    */

    public void addScore(int points) { //score system
        this.score += points;
    }

    public int getScore(){
        return  score;
    }
}

