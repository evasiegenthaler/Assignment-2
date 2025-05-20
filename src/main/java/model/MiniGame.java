package model;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MiniGame {

    // Case 5.1: Number Guessing Game
    public void numberGuessingGame(Player player, Random random, Scanner scanner) {
        int randomNumber = random.nextInt(100) + 1;
        int attempt = 7;
        int reward;

        System.out.println("\n******* Welcome to Number Guessing Game! *******");
        for (int i = 1; i <= attempt; i++) {
            try {
                System.out.print("\nAttempt " + i + ": Guess a number between 1 and 100: ");
                int guess = scanner.nextInt();

                if (guess == randomNumber) {
                    reward = 8 - i; // base reward
                    reward = Math.min(reward + player.getPets().size() * 2, 15); // balancing and capping reward against number of pets and their hunger increase rate
                    player.setFoodSupply(player.getFoodSupply() + reward);
                    System.out.println("Correct Guess! You have earned " + reward + " units of food. Current Food Supply: " + player.getFoodSupply() + " Units");
                    break;
                } else if (guess > randomNumber) {
                    System.out.println("Your guess is too high! Try again. Attempts remaining: " + (attempt - i));
                } else {
                    System.out.println("Your guess is too low! Try again. Attempts remaining: " + (attempt - i));
                }
            } catch (InputMismatchException exception) {
                System.out.println("Invalid input! Please enter a valid number between 1 and 100.");
                scanner.nextLine(); // to consume the left-over (invalid) input from the previous scanner
                i--; // decrement the attempt counter to allow the user to try again without penalty
            }
            // to handle the case of running out of attempts
            if (i == attempt) {
                System.out.println("No more attempts left! The correct number was: " + randomNumber);
            }
        }
    }

    // Case 5.2: Word Scramble Game
    public void wordScrambleGame(Player player, Random random, Scanner scanner) {
        String[] words = {"bird", "cat", "dog", "fish", "rabbit", "snake", "spider"};
        String randomWord = words[random.nextInt(words.length)];
        String scrambledWord = scrambler(randomWord, random);
        int attempt = 3;
        int reward;

        System.out.println("\n******* Welcome to Word Scrambled Game! *******");
        for (int i = 1; i <= attempt; i++) {
            System.out.print("\nAttempt " + i + ": Unscramble the word '" + scrambledWord + "': ");
            String guess = scanner.next();

            if (guess.equalsIgnoreCase(randomWord)) {
                reward = 4 - i; // base reward
                reward = Math.min(reward + player.getPets().size() * 2, 10); // balancing and capping reward against number of pets and their hunger increase rate
                player.setFoodSupply(player.getFoodSupply() + reward);
                System.out.println("Correct Guess! You have earned " + reward + " units of food. Current Food Supply: " + player.getFoodSupply() + " Units");
                break;
            } else {
                System.out.println("Wrong Guess! Try again. Attempts remaining: " + (attempt - i));
            }
            // to handle the case of running out of attempts
            if (i == attempt) {
                System.out.println("No more attempts left! The correct word was: " + randomWord);
            }
        }
    }

    // Helper method for word scramble game: to scramble the randomWord
    private String scrambler(String randomWord, Random random) {
        char[] letters = randomWord.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            int j = random.nextInt(letters.length);
            char temp = letters[i];
            letters[i] = letters[j];
            letters[j] = temp;
        }
        return new String(letters);
    }

    // Case 5.3: Multiplication Game
    public void multiplicationGame(Player player, Random random, Scanner scanner) {
        // Generate two random numbers between 1 and 100 for the multiplication question.
        int num1 = random.nextInt(100) + 1;
        int num2 = random.nextInt(100) + 1;
        int correctAnswer = num1 * num2; // Calculate the correct answer.

        // Display game instructions and the multiplication question.
        System.out.println("\n******* Welcome to Multiplication Game! *******");
        System.out.print("What is " + num1 + " * " + num2 + "? You have 15 seconds: ");

        long startTime = System.currentTimeMillis(); // Record the start time of the game. Ruft die aktuelle zeit in millisekunden auf um den startpunkt zu speichern
        try {
            int answer = scanner.nextInt(); // Read the user's answer.
            long endTime = System.currentTimeMillis(); // Record the end time of the game.
            double timeTaken = (endTime - startTime) / 1000.0; // Calculate time taken in seconds.

            // Check if the answer is correct and within the time limit.
            if (answer == correctAnswer && timeTaken <= 15) {
                // Calculate the reward based on the formula: 5 + (2 * number of pets), capped at a maximum of 12 units.
                int reward = Math.min(5 + player.getPets().size() * 2, 12);

                // Update the player's food supply by adding the calculated reward.
                player.setFoodSupply(player.getFoodSupply() + reward);

                // Display a message indicating that the answer is correct, along with the reward earned and the time taken.
                System.out.println("Correct! You have earned " + reward + " units of food. Time taken: "
                        + String.format("%.2f", timeTaken) + " seconds.");
            } else if (answer != correctAnswer) {
                // If the answer is incorrect, display a message indicating the failure and provide the correct answer and time taken.
                System.out.println("Incorrect! No food earned. Time taken: "
                        + String.format("%.2f", timeTaken) + " seconds. The correct answer was: " + correctAnswer);
            } else {
                // If the answer is correct but the player exceeded the time limit (15 seconds), display a message indicating no food earned.
                System.out.println("Too slow! No food earned. Time taken: "
                        + String.format("%.2f", timeTaken) + " seconds. The correct answer was: " + correctAnswer); //%.2f= floating point number, format 2 decimal places
            }

        } catch (InputMismatchException exception) { //part of java method
            // Catch block to handle cases where the user inputs an invalid type (e.g., a non-numeric value).

            System.out.println("Invalid input! You must enter a number.");
            // Inform the user that the input was invalid and they need to enter a valid numeric value.

            scanner.nextLine();
            // Clear the scanner buffer by advancing past the invalid input to prevent the program from getting stuck.
        }
    }


    // Case 5.4: Fortune Wheel Game
    public void fortuneWheel(Player player, Random random) {
        int randomNumber = random.nextInt(100) + 1; // Generate a random number between 1 and 100.
        System.out.println("\n******* Welcome to Fortune Wheel Game! *******"); // Display a welcome message.
        System.out.println("The generated number is: " + randomNumber); // Display the generated random number.

        if (isPrime(randomNumber)) { // Check if the number is prime.
            player.setFoodSupply(player.getFoodSupply() + 10); // Reward 10 units of food for a prime number.
            System.out.println("The number is a prime! You have earned 10 units of food.");
        } else if (randomNumber % 3 == 0 && randomNumber % 5 == 0) { // Check if the number is divisible by both 3 and 5.
            player.setFoodSupply(player.getFoodSupply() + 3); // Reward 3 units of food for divisibility by 3 and 5.
            System.out.println("The number is divisible by 3 and 5! You have earned 3 units of food.");
        } else if (randomNumber % 2 == 0 && randomNumber % 4 == 0) { // Check if the number is divisible by both 2 and 4.
            player.setFoodSupply(player.getFoodSupply() + 2); // Reward 2 units of food for divisibility by 2 and 4.
            System.out.println("The number is divisible by 2 and 4! You have earned 2 units of food.");
        } else {
            System.out.println("No rewards this time!"); // Inform the player that no rewards were earned.
        }
    }

    // Helper method for fortune wheel game: to check if a number is prime
    private boolean isPrime(int num) {
        if (num <= 1) return false; // Numbers less than or equal to 1 are not prime.
        for (int i = 2; i <= Math.sqrt(num); i++) { // Check divisibility from 2 up to the square root of the number. So it don't need to interate from 2 to 35 for example
            //If num has a factor greater than its square root, it must also have a corresponding smaller factor below the square root.
            if (num % i == 0) return false; // If divisible by any number in this range, it's not prime.'return' acts like break here to stop the loop.
        }
        return true; // If no divisors are found, the number is prime.
    }
}