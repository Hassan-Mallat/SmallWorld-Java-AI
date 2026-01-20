package model.game;

public class AIPlayer extends Player {

    private AIStrategy strategy;
    
    public AIPlayer(Game _game, String _name){
        super(_game, _name);
        this.strategy = new SmartStrategy();
    }

    // Setter allows changing difficulty, smart ai or dumb XD, easy mode or hard mode
    public void setStrategy(AIStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Play getPlay() {
        System.out.println(name + " (AI) is thinking..."); // more realistic maybe?? :p
        
        try { Thread.sleep(800); } catch (Exception e) {} // to act like it took time to think, not instant lol

        // Delegate logic to the strategy object
        return strategy.calculateMove(this, game.getBoard());
    }
}

