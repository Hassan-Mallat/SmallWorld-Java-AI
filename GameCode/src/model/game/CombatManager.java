package model.game;

import java.util.Random;

public class CombatManager {

    private static final Random random = new Random();

    // combat rules: Roll X dice for attacker, Y dice for defender.
    //  then we compare sums, higher sum wins.

    
    // rolling the dice
    private static int rollDiceSum(int numberOfDice) {

        int sum = 0;
        for (int i = 0; i < numberOfDice; i++) {
            sum += random.nextInt(6) + 1; // generatres random number 1-6
        }

        return sum;
    }
    

    // resolving the attack, win or lose

    public static boolean resolveAttack(int attackerCount, int defenderCount) {

        int attackScore = rollDiceSum(attackerCount);
        int defenseScore = rollDiceSum(defenderCount);

        System.out.println("Combat: Attacker rolled " + attackScore + " vs Defender rolled " + defenseScore);
        
        while (attackScore == defenseScore) { // repeat in case we get the same sum

            System.out.println("It's a tie, rolling again...");

            attackScore = rollDiceSum(attackerCount);
            defenseScore = rollDiceSum(defenderCount);

            System.out.println("Final score: attacker: " + attackScore + " vs Defender: " + defenseScore);
        }
        
        // Return true if attacker wins
        return attackScore > defenseScore;
    }

    
}