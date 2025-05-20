package model;
import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

public class GameManager {
    // Class Attributes
    private final Random random;
    private final Scanner scanner;
    private final GameAction gameAction; //Instanzvariable die dann im Konstruktor definiert wird
    private final MiniGame miniGame;
    private Player player;
    private int turnNumber;

    // Constructor
    public GameManager() {
        random = new Random();
        scanner = new Scanner(System.in);
        gameAction = new GameAction();
        miniGame = new MiniGame();
        turnNumber = 0;
    }

    // Getter for turnNumber
    public int getTurnNumber() {
        return turnNumber;
    }

    // Setter for turnNumber
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    // Main-Method
    public static void main(String[] args) {
        // Create a new instance of the GameManager class to manage the game.
        GameManager gameManager = new GameManager();

        // Call the startGame method to begin the game.
        gameManager.startGame();
    }

    // Method to start the game
    private void startGame() {
        System.out.print("\nEnter a player name to start the game: ");
        String playerName = scanner.next();
        player = new Player(playerName);
        System.out.println("\nWelcome " + playerName + "! How do you want to proceed?");

        boolean isPlaying = true;
        while (isPlaying) {
            if (gameOver()) {
                break;
            }
            try {
                System.out.println("\n1. Adopt a New Pet");
                System.out.println("2. Feed a Pet");
                System.out.println("3. Play with a Pet");
                System.out.println("4. Let a Pet Rest");
                System.out.println("5. Play a Mini-Game");
                System.out.println("6. Display Status");
                System.out.println("7. Sort Pets");
                System.out.println("8. Search for a Pet");
                System.out.println("9. Exit Game");
                System.out.print("\nChoose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        gameAction.adoptNewPet(player, scanner); //player, scanner arguments we need to pass to this method to run. That is further on declared in the method
                        break;
                    case 2:
                        gameAction.feedPet(this, player, scanner); //this= verweist auf die aktuelle Instanz der Klasse die diese Methode enthält, hier also gameManager
                        break;
                    case 3:
                        gameAction.playWithPet(this, player, scanner);
                        break;
                    case 4:
                        gameAction.letPetRest(this, player, scanner);
                        break;
                    case 5:
                        gameAction.playMiniGame(miniGame, player, random, scanner);
                        break;
                    case 6:
                        gameAction.playerStatus(player);
                        break;
                    case 7:
                        System.out.println("\n1. Sort by Name (Ascending Order)");
                        System.out.println("2. Sort by Happiness Level (Descending Order)");
                        System.out.print("\nChoose an option: ");

                        try {
                            int sortChoice = scanner.nextInt();
                            switch (sortChoice) {
                                case 1:
                                    gameAction.sortPetsByName(player);
                                    break;
                                case 2:
                                    gameAction.sortPetsByHappiness(player);
                                    break;
                                default:
                                    System.out.println("Invalid choice! Please select 1 or 2.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter 1 or 2.");
                            scanner.nextLine(); // Clear invalid input
                        }
                        break;
                    case 8:
                        gameAction.searchForPet(player, scanner);
                        break;
                    case 9:
                        isPlaying = false;
                        System.out.println("Thanks for playing " + player.getPlayerName() + "!\nTotal turns survived: " + turnNumber);
                        break;
                    default:
                        System.out.println("Invalid option! Please choose a number between 1 and 9.");
                }
            } catch (InputMismatchException exception) {
                System.out.println("Invalid input! Please enter a valid number between 1 and 9.");
                scanner.nextLine();
            }
        }
    }

    // Method to check if all pets are starving
    private boolean allPetsStarving() {
        for (Pet pet : player.getPets()) { // getPets=Liste aller Haustiere
            if (pet.getHunger() <= 80) { // Schaut ob es mindestens noch ein Haustier gibt. Falls es das gibt also haustier unter 80
                return false; //dann gibt es zurück false=somit sind nicht alle Haustiere am verhungern
            }
        }
        return true;
    }

    // Method to check the game over condition
    private boolean gameOver() {
        if (player.getFoodSupply() == 0 && allPetsStarving()) {
            System.out.println("\n!!!Game Over!!!\nYou have run out of food and all pets are starving!");
            System.out.println("Total turns survived: " + turnNumber);
            return true;
        }
        return false;
    }
}
