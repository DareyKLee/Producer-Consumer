import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The TableWaiter class creates thread that serve/handle client requests
 */
public class TableWaiter extends Thread{
    private Socket customer;        //A TableWaiter utilizes a unique socket to communicate with their designated client
    private Restaurant restaurant;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * The constructor for TableWaiter
     * @param customer is the socket assigned to a specific customer
     * @param restaurant represents the current restaurant the chef is working in
     */
    public TableWaiter(Socket customer, Restaurant restaurant){
        this.customer = customer;
        this.restaurant = restaurant;

        try {
            in = new BufferedReader(new InputStreamReader(customer.getInputStream()));
            out = new PrintWriter(customer.getOutputStream(), true);

        } catch (IOException IOEX){
            IOEX.printStackTrace();
        }
    }

    /**
     * Waiters continuously wait for a client request, take food from the food tray, and reply to the client to "serve"
     * the customer. If there is no food in the food tray and no more chefs, tell the client that there is nothing more
     * to serve and close the socket to finish execution.
     */
    @Override
    public void run(){
        String customerName = "";

        try {
            do {
                customerName = in.readLine();       //wait for and then read the next message received from the client,
                                                    //in this program the client sends their own name to make requests

                System.out.println("Request from " + customerName);

                if (restaurant.closeShop()){    //call closeShop() to check if there are no items present in the bounded
                    break;                      //buffer and no more producers to produce more items, so it can escape
                }                               //this loop to complete execution

                restaurant.takeFood();          //call takeFood() to remove an item from the bounded buffer

                out.println("SERVED " + customerName);  //send a message to the client to simulate serving the customer

            } while (true);

            out.println("NO MORE FOOD");        //send a message to the client to inform them there is no more to serve

            System.out.println("WAITER SERVING " + customerName + " ENDING SHIFT.");

            restaurant.waiterEndShift();        //call waiterEndShift() to decrement the value representing the number
                                                //of TableWaiters threads present in the Restaurant object
        } catch (IOException IOEX) {
            IOEX.printStackTrace();

        } finally {     //close the socket used to communicate with the client because there is nothing more to serve
            try {
                System.out.println("**CLOSING WAITER SERVING " + customerName + " SOCKET**");
                customer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
