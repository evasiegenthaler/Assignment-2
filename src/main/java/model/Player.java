package model;

import java.util.ArrayList;

public class Player {
    // Class Attributes
    private final String playerName;
    private int foodSupply;
    private final ArrayList<Pet> pets;

    private static final int maxPets = 5; // define that you can not have more than 5 Pets

    // Constructor
    public Player(String playerName) {
        this.playerName = playerName;
        this.foodSupply = 50;
        this.pets = new ArrayList<>();
    }

    // Getter for playerName
    public String getPlayerName() {
        return playerName;
    }

    // Getter for foodSupply
    public int getFoodSupply() {
        return foodSupply;
    }

    // Getter for 'pets' ArrayList
    public ArrayList<Pet> getPets() {
        return pets;
    }

    // Getter for maxPets
    public int getMaxPets() {
        return maxPets;
    }

    // Setter for foodSupply
    public void setFoodSupply(int foodSupply) {
        this.foodSupply = foodSupply;
    }

    // Method to add pet to 'pets' ArrayList
    public void addPet(Pet pet) {
        pets.add(pet);
    }

    // Method to find pet by name
    public ArrayList<Pet> findPetsByName(String petName) { // methode erwartet einen string petName als argument
        ArrayList<Pet> matchingPets = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getPetName().equalsIgnoreCase(petName)) { //still can use here pet.getPetName as it is same behaviour so
                //you search in both subclasses in the same way for a pet
                matchingPets.add(pet);
            }
        }
        return matchingPets; //return the list machingPets
    }

    // Method to find pet by ID
    public Pet findPetById(int petId) {
        for (Pet pet : pets) {
            if (pet.getId() == petId) {
                return pet;
            }
        }
        return null;
    }

        // Display the status of each pet owned by the player.
//        for (Pet pet : pets) {
//            System.out.println(pet.getStatus() + "\n"); // Print each pet's status (e.g., hunger, happiness, etc.).
//        }
    }


