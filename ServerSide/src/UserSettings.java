import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * The UserSettings class here is a husk used to recreate the foreign UserSettings object received from the client so
 * that the variables of that object can be accessed locally.
 */
public class UserSettings implements Serializable{
    private int numCustomers;
    private int trayCapacity;
    private int ingredientsAvailable;
    private int numChefs;

    public int getNumCustomers() {
        return numCustomers;
    }

    public int getTrayCapacity() {
        return trayCapacity;
    }

    public int getIngredientsAvailable() {
        return ingredientsAvailable;
    }

    public int getNumChefs() {
        return numChefs;
    }
}
