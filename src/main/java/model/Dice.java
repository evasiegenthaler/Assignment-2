package model;
import java.util.Random;

public class Dice {
    public int makeRandomNumber() {
        Random random = new Random(); // Create a Random object
        int[] numberOfSidesDice = {1, 2, 3, 4, 5, 6}; // Array for dice sides
        int randomNumber = numberOfSidesDice[random.nextInt(numberOfSidesDice.length)]; // Pick a random index
        return randomNumber; // Return the random number
    }

    public static void main(String[] args) {
        Dice dice = new Dice(); // Create a Dice object
        int roll = dice.makeRandomNumber(); // Roll the dice
        System.out.println("You rolled: " + roll); // Print the result
    }
}