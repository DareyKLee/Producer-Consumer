import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * UserSettings class is used for creating objects that contain the variables that dictate the number of consumer
 * and producer threads to spawn, the size of the bounded buffer, and the maximum amount of items that will be produced
 * and consumed before the program terminates
 */
public class UserSettings implements Serializable{
    private int numCustomers;           //number of CustomerThreads
    private int trayCapacity;           //bounded buffer
    private int ingredientsAvailable;   //max amount of items that will be produced/consumed
    private int numChefs;               //number of ChefThreads

    /**
     * The constructor for UserSettings organizes a dialog with the user who enters the parameters that controls
     * the simulation
     */
    public UserSettings(){
        try {
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the amount of customers (consumers): ");
            this.numCustomers = Integer.parseInt(userEntry.readLine());

            System.out.print("Enter the size of food tray (bounded buffer): ");
            this.trayCapacity = Integer.parseInt(userEntry.readLine());

            System.out.print("Enter the amount of ingredients available " +
                             "(amount produced/consumed before termination): ");
            this.ingredientsAvailable = Integer.parseInt(userEntry.readLine());

            System.out.print("Enter the number of chefs (producers): ");
            this.numChefs = Integer.parseInt(userEntry.readLine());

        } catch (IOException IOEX){
            IOEX.printStackTrace();
        }
    }

    /**
     * This returns the number of customers (consumer threads)
     * @return number of customers
     */
    public int getNumCustomers() {
        return numCustomers;
    }

    /**
     * This returns the capacity of the food tray (bounded buffer)
     * @return the tray capacity
     */
    public int getTrayCapacity() {
        return trayCapacity;
    }

    /**
     * This returns the ingredients available (maximum amount produced/consumed)
     * @return ingredients available
     */
    public int getIngredientsAvailable() {
        return ingredientsAvailable;
    }

    /**
     * This returns the number of chefs (producer threads)
     * @return the number of chefs
     */
    public int getNumChefs() {
        return numChefs;
    }
}
