package model;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class GameAction {

    // Case 1: Method to adopt a new pet
    public void adoptNewPet(Player player, Scanner scanner) {
        if (player.getFoodSupply() < (player.getPets().size() * 5 + player.getPets().size() * 2)) { //5 because max 5 pets and each pet needs at least 1 food supply
            //makes dynamic depends on how many pets you have if you can adopt a new one or not
            System.out.println("\nNot enough food supply to adopt a new Pet. Please play a mini-game to earn more food.\n");
        } else if (player.getPets().size() == player.getMaxPets()) {
            System.out.println("\nYou have reached the maximum limit of adopting 5 pets.\n");
        } else {
            Pet newPet = null;

            boolean validInput = true;

            while (validInput) {
                try {
                    System.out.print("\nEnter 1 to adopt a 'Normal Pet' or 2 to adopt a 'Mighty Pet': ");
                    int petType = scanner.nextInt();

                    if (petType == 1 || petType == 2) {
                        System.out.print("Enter a name for your newly adopted pet: ");
                        String petName = scanner.next();

                        if (petType == 1) {
                            newPet = new NormalPet(petName,50,50,50); //create Pet with the petName=user input
                            System.out.println("Your normal pet '" + petName + "' is assigned with (ID: " + newPet.getId() + ").\n\n" + newPet.getStatus());
                        } else {
                            newPet = new MightyPet(petName);
                            System.out.println("Your mighty pet '" + petName + "' is assigned with (ID: " + newPet.getId() + ").\n\n" + newPet.getStatus());
                        }

                        validInput = false; // Exit the loop after creating a pet
                    } else {
                        System.out.println("Invalid option! Please enter 1 or 2.");
                    }
                } catch (InputMismatchException exception) {
                    System.out.println("Invalid input! Please enter a number (1 or 2).");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            player.addPet(newPet);
        }
    }

    // Helper method to update all pets' status & to warn player and remove pet with maximum hunger level
    private void updateStatusAllPet(Player player) {
        ArrayList<Pet> petsToRemove = new ArrayList<>();
        for (Pet pet : player.getPets()) {
            pet.updateStatus();
            if (pet.getHunger() > 80 && pet.getHunger() < 100) {
                System.out.println("\nWarning: " + pet.getPetName() + " is starving! Feed " + pet.getPetName() + ".");
            } else if (pet.getHunger() == 100) {
                System.out.println("\n******* Too Sad! " + pet.getPetName() + " has starved to death! *******");
                petsToRemove.add(pet);
            }
        }
        player.getPets().removeAll(petsToRemove); //takes normal pet list and removes all pets from petsToRemove list
    }

    // Case 2: Method to feed a pet
    public void feedPet(GameManager gameManager, Player player, Scanner scanner) {
        System.out.print("\nEnter pet name to feed: "); // Prompt the player to enter the pet's name.
        String petName = scanner.next(); // Read the pet's name input from the player.
        ArrayList<Pet> matchingPets = player.findPetsByName(petName); // Get a list of pets with the given name.

        if (matchingPets.isEmpty()) { // If no pets are found with the given name.
            System.out.println("No pet with name '" + petName + "' found. Try again!"); // Inform the player that no pets match.
            return; // Exit the method.
        }

        // Check if there is only one pet with the given name.
        if (matchingPets.size() == 1) {
            Pet petToFeed = matchingPets.get(0); // Get the single matching pet which will be on index 0.

            // Feeding the pet directly
            if (player.getFoodSupply() <= 0) {  // Check if the player has enough food to feed the pet.
                System.out.println("Not enough food to feed the pet. Please play a mini-game to earn more food."); // Inform the player about insufficient food.
            } else {
                player.setFoodSupply(player.getFoodSupply() - 1); // Deduct one unit of food from the player's food supply.
                petToFeed.feed(player); // Call the feed method on the pet.
                updateStatusAllPet(player); // Update the statuses of all pets after feeding.
                gameManager.setTurnNumber(gameManager.getTurnNumber() + 1); // Increment the game turn number.
            }
            return; // Exit the method after feeding the pet.
        }

        // If multiple pets with the same name are found, display their details and prompt for ID.
        System.out.println("\nFound the following pets with the name '" + petName + "':");
        for (Pet pet : matchingPets) {
            System.out.println("ID: " + pet.getId() + " | " + pet.getStatus()); // Display the ID and status of each matching pet.
        }

        // Prompt the user to select a pet by its unique ID.
        System.out.print("\nEnter the unique ID of the pet to feed: ");
        int petId;
        try {
            petId = scanner.nextInt(); // Read the pet ID input from the player.
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID."); // Handle invalid (non-numeric) input.
            scanner.nextLine(); // Clear the invalid input.
            return; // Exit the method.
        }

        // Find the pet with the given ID.
        Pet petToFeed = player.findPetById(petId); // Retrieve the pet with the specified ID.
        if (petToFeed == null || !matchingPets.contains(petToFeed)) { // If no matching pet found
            System.out.println("No pet with ID '" + petId + "' found among the matching pets. Try again!"); // Inform the player of the invalid ID.
            return; // Exit the method.
        }

        // Check if the player has enough food to feed the selected pet.
        if (player.getFoodSupply() <= 0) {
            System.out.println("Not enough food to feed the pet. Please play a mini-game to earn more food."); // Inform about insufficient food.
        } else {
            player.setFoodSupply(player.getFoodSupply() - 1); // Deduct one unit of food from the player's supply.
            petToFeed.feed(player); // Feed the selected pet. No need to check instance of because it already know the pet type and the class is abstract
            updateStatusAllPet(player); // Update the statuses of all pets after feeding.
            gameManager.setTurnNumber(gameManager.getTurnNumber() + 1); // Increment the game turn number.
        }
    }


    // Case 3: Method to play with the pet
    public void playWithPet(GameManager gameManager, Player player, Scanner scanner) {
        System.out.print("\nEnter pet name to play with: ");
        String petName = scanner.next();
        ArrayList<Pet> matchingPets = player.findPetsByName(petName); // Get all matching pets

        if (matchingPets.isEmpty()) { // No pets found with the given name
            System.out.println("No pet with name '" + petName + "' found. Try again!");
            return;
        }

        // Check if there is only one pet with the given name
        if (matchingPets.size() == 1) {
            Pet petToPlay = matchingPets.get(0); // Get the single matching pet

            // Play with the pet directly
            petToPlay.play();
            updateStatusAllPet(player);
            gameManager.setTurnNumber(gameManager.getTurnNumber() + 1);
            return;
        }

        // If multiple pets are found, display details and prompt for ID
        System.out.println("\nFound the following pets with the name '" + petName + "':");
        for (Pet pet : matchingPets) {
            System.out.println("ID: " + pet.getId() + " | " + pet.getStatus());
        }

        // Prompt the user to select a pet by ID
        System.out.print("\nEnter the unique ID of the pet to play with: ");
        int petId;
        try {
            petId = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID.");
            scanner.nextLine(); // Clear invalid input
            return;
        }

        // Find the pet by ID
        Pet petToPlay = player.findPetById(petId);
        if (petToPlay == null || !matchingPets.contains(petToPlay)) {
            System.out.println("No pet with ID '" + petId + "' found among the matching pets. Try again!");
            return;
        }

        // Play with the pet
        petToPlay.play();
        updateStatusAllPet(player);
        gameManager.setTurnNumber(gameManager.getTurnNumber() + 1);
    }

    // Case 4: Method to let the pet rest
    public void letPetRest(GameManager gameManager, Player player, Scanner scanner) {
        System.out.print("\nEnter pet name to let it rest: ");
        String petName = scanner.next();
        ArrayList<Pet> matchingPets = player.findPetsByName(petName); // Get all matching pets

        if (matchingPets.isEmpty()) { // No pets found with the given name
            System.out.println("No pet with name '" + petName + "' found. Try again!");
            return;
        }

        // Check if there is only one pet with the given name
        if (matchingPets.size() == 1) {
            Pet petToRest = matchingPets.get(0); // Get the single matching pet

            // Let the pet rest directly
            petToRest.rest();
            updateStatusAllPet(player);
            gameManager.setTurnNumber(gameManager.getTurnNumber() + 1);
            return;
        }

        // If multiple pets are found, display details and prompt for ID
        System.out.println("\nFound the following pets with the name '" + petName + "':");
        for (Pet pet : matchingPets) {
            System.out.println("ID: " + pet.getId() + " | " + pet.getStatus());
        }

        // Prompt the user to select a pet by ID
        System.out.print("\nEnter the unique ID of the pet to let rest: ");
        int petId;
        try {
            petId = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a valid numeric ID.");
            scanner.nextLine(); // Clear invalid input
            return;
        }

        // Find the pet by ID
        Pet petToRest = player.findPetById(petId);
        if (petToRest == null || !matchingPets.contains(petToRest)) {
            System.out.println("No pet with ID '" + petId + "' found among the matching pets. Try again!");
            return;
        }

        // Let the pet rest
        petToRest.rest();
        updateStatusAllPet(player);
        gameManager.setTurnNumber(gameManager.getTurnNumber() + 1);
    }

    // Case 5: Method to play a mini-game
    public void playMiniGame(MiniGame miniGame, Player player, Random random, Scanner scanner) {
        boolean validInput = true; // Flag to control the input validation loop.

        // Loop until the player provides valid input.
        while (validInput) {
            try {
                // Display the available mini-game options to the player.
                System.out.print("\n1. Number Guessing Game\n2. Word Scramble Game\n3. Multiplication Game\n4. Fortune Wheel Game\n\nChoose an option: ");

                int choice = scanner.nextInt(); // Read the player's choice.

                // Use a switch-case to handle the player's choice.
                switch (choice) {
                    case 1:
                        // Start the Number Guessing Game.
                        miniGame.numberGuessingGame(player, random, scanner); //just player as Datatype is already defined in playMiniGame Player player
                        validInput = false; // Exit the loop after a valid game is chosen.
                        break;
                    case 2:
                        // Start the Word Scramble Game.
                        miniGame.wordScrambleGame(player, random, scanner);
                        validInput = false; // Exit the loop after a valid game is chosen.
                        break;
                    case 3:
                        // Start the Multiplication Game.
                        miniGame.multiplicationGame(player, random, scanner);
                        validInput = false; // Exit the loop after a valid game is chosen.
                        break;
                    case 4:
                        // Start the Fortune Wheel Game.
                        miniGame.fortuneWheel(player, random);
                        validInput = false; // Exit the loop after a valid game is chosen.
                        break;
                    default:
                        // Handle invalid options that are not between 1 and 4.
                        System.out.println("Invalid option! Please choose a number between 1 and 4.");
                }
            } catch (InputMismatchException exception) {
                // Handle invalid input (e.g., non-numeric values).
                System.out.println("Invalid input! Please enter a valid number between 1 and 4.");
                scanner.nextLine(); // Clear the invalid input from the scanner buffer.
            }
        }
    }

    // Case 6: Method to display player status
    public void playerStatus(Player player) {
        // Display the player's name.
        System.out.println("\nPlayer Name: " + player.getPlayerName());

        // Display the player's current food supply.
        System.out.println("Food Supply: " + player.getFoodSupply() + " Units");

        // Display the total number of pets owned by the player.
        System.out.println("Total Number of Pets: " + player.getPets().size() + "\n");

        // Initialize counters for Normal and Mighty pets.
        int normalPetCount = 0; // Counter for Normal Pets.
        int mightyPetCount = 0; // Counter for Mighty Pets.

        // Iterate through the list of pets to classify them.
        for (Pet pet : player.getPets()) {
            if (pet instanceof NormalPet) { // Check if the pet is a NormalPet.
                normalPetCount++; // Increment the counter for Normal Pets.
            } else { // If not a NormalPet, it must be a MightyPet.
                mightyPetCount++; // Increment the counter for Mighty Pets.
            }
        }

        System.out.println("Number of Normal Pets: " + normalPetCount);  // Display the number of Normal Pets.
        System.out.println("Number of Mighty Pets: " + mightyPetCount + "\n"); // Display the number of Mighty Pets.
    }



        // Case 7.1: Method to sort pets by name
    public void sortPetsByName(Player player) {
        // Sort the pets list in ascending order of their names (case-insensitive).
        player.getPets().sort((p1, p2) -> p1.getPetName().compareToIgnoreCase(p2.getPetName())); // for ascending, p1 comes first, then p2
        //A negative value if p1.getPetName() comes before p2.getPetName() alphabetically. so p1 is placed before p2
        //A positive value if p1.getPetName() comes after p2.getPetName() alphabetically. so p1 is placed after p2
        //as long the swap is the method goes. Tim peters 2002 sorting method=tim sort

        //p1,p2=lambda expression that acts like a comperator. p1/p2 represent two pets in the list
        //compareIgnoreCase method from String class/ sort=repeatadly calls lambda with pairs of elements
        //charlie=p1 c=99 bella p2=98 bella".compareToIgnoreCase("Archie") → Positive so p1 is placed after p2
        //bella".compareToIgnoreCase("Charlie") → negative so p1 before p2
        // Lambda decides order= negative integer when p1 comes before p2, 0=pet the same, positive integer if p1 comes after p2

        // Inform the player that the pets have been sorted.
        System.out.println("\nPets sorted by name (ascending order):");

        // Loop through the sorted list of pets and display their status.
        for (Pet pet : player.getPets()) {
            System.out.println(pet.getStatus() + "\n"); // Print each pet's status (e.g., hunger, happiness, etc.).
        }
    }


    // Case 7.2: Method to sort pets by happiness level
    public void sortPetsByHappiness(Player player) {
        // Sort the pets list in descending order of their happiness levels.
        player.getPets().sort((p1, p2) -> Integer.compare(p2.getHappiness(), p1.getHappiness())); // for descending, p2 comes first, then p1
        //A positive value if p2.getHappiness() > p1.getHappiness() → p2 should come before p1.
        //A negative value if p2.getHappiness() < p1.getHappiness() → p1 should come before p2.
        //goes as long as there is no more swap
        // Inform the player that the pets have been sorted by happiness level.
        System.out.println("\nPets sorted by happiness level (descending order):");

        // Loop through the sorted list of pets and display their status.
        for (Pet pet : player.getPets()) {
            System.out.println(pet.getStatus() + "\n"); // Print each pet's status (e.g., name, hunger, happiness, etc.).
        }
    }


    // Case 8: Method to search for a pet (can be one pet or multiple pets)
    public void searchForPet(Player player, Scanner scanner) {
        System.out.print("\nEnter pet name to search: ");

        String petName = scanner.next(); // Read the pet name input from the player.
        ArrayList<Pet> matchingPets = player.findPetsByName(petName);    // Retrieve a list of pets with names that match the entered pet name.

        if (matchingPets.isEmpty()) {  // Check if the list of matching pets is empty (no matches found).
            System.out.println("No pet with name '" + petName + "' found.");
        } else {
            System.out.println("\nFound the following pets with the name '" + petName + "':");

            for (Pet pet : matchingPets) {  // Iterate through the list of matching pets and display their details.
                System.out.println("ID: " + pet.getId() + " | " + pet.getStatus());  // Print the pet's unique ID and its status (e.g., hunger, happiness).
            }
        }
    }
}
// Comparator<Pet> petNameComparator = Comparator.comparing(Pet::getPetName, String.CASE_INSENSITIVE_ORDER);
