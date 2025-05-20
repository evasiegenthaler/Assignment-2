package model;

// Extending Pet class to create a MightyPet subclass
public class MightyPet extends Pet {

    // Constructor
    public MightyPet( String name) {
        super(name); // super keyword is calling the Pet constructor from the parent Pet class.
    }

    // Override feed method for MightyPet
    @Override
    public void feed(Player player) { //this method needs argument player
        setHappiness(getHappiness() + 15);
        setHunger(getHunger() - 25);
        System.out.println(getPetName() + " has been fed. Remaining Food Supply: " + player.getFoodSupply() + " Units\n");
        System.out.println(getStatus());
    }

    // Override play method for MightyPet
    @Override
    public void play() {
        setHappiness(getHappiness() + 20);
        setHunger(getHunger() + 10);
        setEnergy(getEnergy() - 10);
        System.out.println(getPetName() + " had fun playing!\n");
        System.out.println(getStatus());
    }

    // Override rest method for MightyPet
    @Override // override a method from the parent class, overload is a method with the same name in the same class and the compiler time decide which method gets executed first=whichmethod comes first
    public void rest() {
        setHunger(getHunger() + 2);
        setEnergy(getEnergy() + 30);
        System.out.println(getPetName() + " is resting.\n");
        System.out.println(getStatus());
    }

    // Override updateStatus method for MightyPet
    @Override
    public void updateStatus() {
        setHappiness(getHappiness() - 2);
        setHunger(getHunger() + 1);
        setEnergy(getEnergy() - 1);
    }
}
