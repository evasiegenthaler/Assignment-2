package model;

import java.util.Objects;

public abstract class Pet {
    // Class Attributes
    private static int nextId = 1;
    private final int id;
    private  String petName; //Ich will dass Name änderbar ist auf PetCard
    private int happiness;
    private int hunger;
    private int energy;

    private static final int maxVal = 100;
    private static final int minVal = 0;

    // Constructor
    public Pet(String name) {
        this.id = nextId++;
        this.petName = name;
        this.happiness = 50;
        this.hunger = 50;
        this.energy = 50;
    }


    // Getter for pet id
    public int getId() {
        return id;
    }

    // Getter for petName
    public String getPetName() {
        return petName;
    }

    // Getter for pet happiness
    public int getHappiness() {
        return happiness;
    }

    // Getter for pet hunger
    public int getHunger() {
        return hunger;
    }

    // Getter for pet energy
    public int getEnergy() {
        return energy;
    }

    // Setter for pet happiness
    public void setPetName(String name) {
        this.petName= name;
    }

    // Setter for pet happiness
    public void setHappiness(int happiness) {
        this.happiness = Math.max(minVal, Math.min(maxVal, happiness));
    }

    // Setter for pet hunger
    public void setHunger(int hunger) {
        this.hunger = Math.max(minVal, Math.min(maxVal, hunger));
    }

    // Setter for pet energy
    public void setEnergy(int energy) {
        this.energy = Math.max(minVal, Math.min(maxVal, energy));
    }

    // Method to return happiness status
    public String disHappiness() {
        if (happiness == maxVal) {
            return getPetName() + " is very happy.";
        } else if (happiness > 75) {
            return getPetName() + " is happy.";
        } else if (happiness > 50) {
            return getPetName() + " is a little happy.";
        } else if (happiness > 25) {
            return getPetName() + " is a little sad.";
        } else if (happiness > minVal) {
            return getPetName() + " is sad.";
        } else {
            return getPetName() + " is very sad.";
        }
    }

    // Method to return hunger status
    public String disHunger() {
        if (hunger == maxVal) {
            return getPetName() + " is starving.";
        } else if (hunger > 75) {
            return getPetName() + " is very hungry.";
        } else if (hunger > 50) {
            return getPetName() + " is hungry.";
        } else if (hunger > 25) {
            return getPetName() + " is a little hungry.";
        } else if (hunger > minVal) {
            return getPetName() + " is fine.";
        } else {
            return getPetName() + " is full.";
        }
    }

    // Method to return energy status
    public String disEnergy() {
        if (energy == maxVal) {
            return getPetName() + " is full of energy.";
        } else if (energy > 75) {
            return getPetName() + " is very energized.";
        } else if (energy > 50) {
            return getPetName() + " is energized.";
        } else if (energy > 25) {
            return getPetName() + " is little energized.";
        } else if (energy > minVal) {
            return getPetName() + " is little tired.";
        } else {
            return getPetName() + " is out of energy.";
        }
    }

    // Abstract method to feed the pet
    // signature is (method name + parameters) only
    public abstract void feed(Player player); //arguments because needs access to the player attributes

    // Abstract method to play with the pet
    public abstract void play();

    // Abstract method to let the pet rest
    public abstract void rest();

    // Abstract method to update the status of the pet after each turn
    public abstract void updateStatus();

    // Method to return the current status of the pet
    public String getStatus() {
        String petType;
        if (this instanceof NormalPet) {
            petType = "Normal";
        } else {
            petType = "Mighty";
        }
        return "******* Status of " + getPetName() + " (ID: " + getId() + ") *******" +
                "\nPet Type: " + petType +
                "\nHappiness: " + getHappiness() + " | " + disHappiness() +
                "\nHunger: " + getHunger() + " | " + disHunger() +
                "\nEnergy: " + getEnergy() + " | " + disEnergy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return happiness == pet.happiness &&
                hunger == pet.hunger &&
                energy == pet.energy &&
                Objects.equals(petName, pet.petName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petName, happiness, hunger, energy);
    }
}