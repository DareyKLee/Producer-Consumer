/**
 * The ChefThread class is used to create threads that are the producers in the system
 */
public class ChefThread extends Thread {
    private String name;
    private Restaurant restaurant;
    private int amountProduced = 0;     //personal counter for how much a producer produced

    /**
     * The constructor for ChefThread
     * @param number is the numerical order it is generated in inside a for loop
     * @param restaurant represents the current restaurant the chef is working in
     */
    public ChefThread(int number, Restaurant restaurant){
        this.name = "Chef #" + number;
        this.restaurant = restaurant;
    }

    /**
     * Chefs continuously take ingredients, utilize between 2 to 7 seconds to cook food, and increment amountProduced.
     * If a chef is unable to take ingredients, then the chef announces what they produced and finish execution.
     */
    @Override
    public void run(){
        try {
            while (restaurant.takeIngredients()){   //check if it is possible for a producer to get ingredients to cook

                sleep ((int) (Math.random() * 5000) + 2000);    //sleep between 2 to 7 seconds before calling
                restaurant.cookFood();                                //cookFood() to simulate the time required
                                                                      //to cook food

                System.out.println(name + " done cooking.");
                amountProduced++;                                     //increment personal production count
            }

            System.out.println(name + " produced " + amountProduced + " units of food. NOW ENDING SHIFT.");

            restaurant.chefEndShift();      //call chefEndShift() to decrement the value representing the number of
                                            //producer threads in the Restaurant object and then complete its execution
        } catch (InterruptedException interruptedEX) {
            interruptedEX.printStackTrace();
        }
    }
}