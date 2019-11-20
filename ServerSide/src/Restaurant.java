/**
 * The Restaurant class is used for creating objects that act as a monitor in this program. All the synchronized
 * methods and shared variables reside in a Restaurant object.
 */
public class Restaurant {
    private int trayCapacity;               //bounded buffer
    private int ingredientsAvailable;       //maximum amount produced/consumed before program terminates
    private int chefs;                      //number of producer threads (ChefThreads)
    private int waiters;                    //number of TableWaiter threads which correspond with the number of customers

    private int foodInTray = 0;             //initial number of items present in the bounded buffer

    /**
     * The constructor of Restaurant
     * @param trayCapacity the limit of the bounded buffer
     * @param ingredientsAvailable maximum amount produced/consumed
     * @param chefs number of chefs
     * @param numCustomers number of customers
     */
    public Restaurant(int trayCapacity, int ingredientsAvailable, int chefs, int numCustomers){
        this.trayCapacity = trayCapacity;
        this.ingredientsAvailable = ingredientsAvailable;
        this.chefs = chefs;
        this.waiters = numCustomers;
    }

    /**
     * This is a synchronized method for ChefThreads to take ingredients so that a chef can cook.
     * @return TRUE if a chef is able to take ingredients, else return FALSE
     */
    public synchronized boolean takeIngredients(){
        if (ingredientsAvailable > 0) {
            ingredientsAvailable--;
            return true;

        } else {
            return false;
        }
    }

    /**
     * This is a synchronized method for ChefThreads to add the food they cooked into the food tray. A chef waits to add
     * to the food if the food tray is at capacity.
     */
    public synchronized void cookFood(){
        try {
            while (foodInTray >= trayCapacity) {
                wait();
            }

            foodInTray++;

            notify();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * This is a synchronized method for waiters to take food from the food tray to serve to customers. A waiter waits
     * to take food from the food tray if it is empty.
     */
    public synchronized void takeFood(){
        try {
            while (foodInTray <= 0){
                wait();
            }

            foodInTray--;

            notify();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * This is a synchronized method for a chef to clock out when they have completed their work. Chefs decrement the
     * number of chefs in the restaurant to signify that they are leaving.
     */
    public synchronized void chefEndShift(){
        chefs--;
        System.out.println(chefs + " CHEFS REMAINING");
    }

    /**
     * This is a synchronized method for a waiter to clock out when they have completed their work. Waiters decrement
     * the number of waiters in the restaurant to signify that they are leaving.
     */
    public synchronized void waiterEndShift(){
        waiters--;
        System.out.println(waiters + " WAITERS REMAINING");

    }

    /**
     * This is a synchronized method for waiters to check if they can leave their work.
     * @return TRUE if food present or if a chef is present, else return FALSE
     */
    public synchronized boolean closeShop(){
        if (chefs == 0 && foodInTray == 0){
            return true;

        } else {
            return false;
        }
    }
}
