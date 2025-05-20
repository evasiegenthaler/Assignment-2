package model;
public class NormalPet extends Pet {

    // Constructor
    public NormalPet(String name,int happiness, int hunger, int energy ) {
        super(name); // super keyword is calling the Pet constructor from the parent Pet class.
        setHappiness(happiness);
        setHunger(hunger);
        setEnergy(energy);
    }

    @Override
    public void feed(Player player) {
        setHappiness(getHappiness() + 10);
        setHunger(getHunger() - 20);
        System.out.println(getPetName() + " has been fed. Remaining Food Supply: " + player.getFoodSupply() + " Units\n");
        System.out.println(getStatus());
    }

    // Method to play with the pet
    @Override
    public void play() {
        setHappiness(getHappiness() + 15);
        setHunger(getHunger() + 10);
        setEnergy(getEnergy() - 15);
        System.out.println(getPetName() + " had fun playing!\n");
        System.out.println(getStatus());
    }

    // Method to let the pet rest
    @Override
    public void rest() {
        setHunger(getHunger() + 5);
        setEnergy(getEnergy() + 20);
        System.out.println(getPetName() + " is resting.\n");
        System.out.println(getStatus());
    }

    // Method to update the status of the pet after each turn
    @Override
    public void updateStatus() {
        setHappiness(getHappiness() - 3);
        setHunger(getHunger() + 5);
        setEnergy(getEnergy() - 2);
    }
}
