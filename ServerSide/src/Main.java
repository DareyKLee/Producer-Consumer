import java.io.*;
import java.net.*;

/**
 * This application is an implementation of a server that receives a UserSettings object to generate the required
 * amount of producer threads and sockets as specified by the values inside the object. It also handles the interactions
 * that the restaurant staff members make with the customer and with objects present inside the restaurant simulation
 * (server side processing of producer threads).
 *
 * @author Darey Lee
 */
public class Main {
    private static ServerSocket serverSocket;
    private static final int port = 9999;
    private static Restaurant restaurant;

    /**
     * main is responsible for opening a server socket with the specified port and calling initialize()
     */
    public static void main(String[] args) {
        System.out.println("Opening port " + port + "...\n");

        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            System.out.println("Unable to attach to port");
            System.exit(1);
        }

        initialize();
    }

    /**
     * initialize() is responsible for receiving an UserSettings object from the client, replying to the client to
     * verify reception, create a Restaurant object with parameters obtained from the UserSettings object, and to spin
     * up the specified amount of ChefThreads (producers) and TableWaiters (threads responsible for serving individual
     * client threads)
     */
    public static void initialize() {
        Socket socket = null;
        int numCustomers = 0;
        int trayCapacity = 0;
        int ingredientsAvailable = 0;
        int numChefs = 0;

        try {
            System.out.println("Awaiting parameters from client...\n");

            socket = serverSocket.accept();     //establish a socket from next message server socket accepts which is
                                                //a UserSettings object from the client in this case

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());  //ObjectInputStream for the
                                                                                    //UserSettings object from client

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            UserSettings settings = (UserSettings) in.readObject(); //create a UserSettings object locally from
                                                                    //from the UserSettings object received from client
                                                                    //to access getters to set the following values
            numCustomers = settings.getNumCustomers();
            trayCapacity = settings.getTrayCapacity();
            ingredientsAvailable = settings.getIngredientsAvailable();
            numChefs = settings.getNumChefs();

            out.println("***CONTACT ESTABLISHED*** "                //reply to the client to confirm that the
                    + "Initializing with parameters: "              //UserSettings object has been received and
                    + "number of customers = " + numCustomers       //what values the program will execute with
                    + ", food tray capacity = " + trayCapacity
                    + ", total ingredients available = " + ingredientsAvailable
                    + ", number of chefs = " + numChefs + ".");

            System.out.println("Parameters received, now starting...\n");

        } catch (IOException IOEX) {
            IOEX.printStackTrace();

        } catch (ClassNotFoundException classNotFound) {
            classNotFound.printStackTrace();

        } finally {     //close this socket used for receiving UserSetting objects since it is no longer required
            try {
                System.out.println("**CLOSING SOCKET DESIGNATED FOR RECEIVING PARAMETERS**");
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        restaurant = new Restaurant(trayCapacity,               //create a new Restaurant object with the parameters
                                    ingredientsAvailable,       //specified in the UserSettings object received from
                                    numChefs,                   //the client
                                    numCustomers);

        for (int i = 0; i < numChefs; i++) {                    //utilize a for loop to spin up the specified amount
            new ChefThread(i + 1, restaurant).start();  //of ChefThreads
        }

        for (int i = 0; i < numCustomers; i++) {                //utilize a for loop to spin up the specified amount
            newCustomer();                                      //of TableWaiter threads through a call to newCustomer()
        }
    }

    /**
     * newCustomer() is responsible for assigning a new TableWaiter thread for each unique customer (client thread), a
     * new socket is established for every new customer which the TableWaiter utilizes to serve its designated customer
     */
    private static void newCustomer(){
        try {
            Socket customer = serverSocket.accept();    //establish a socket from the first message received from a
                                                        //costumer thread on the client side
            System.out.println("\n**New customer accepted**");

            TableWaiter waiter = new TableWaiter(customer, restaurant);     //create a new TableWaiter thread with
                                                                            //the socket and data present in the
                                                                            //Restaurant object named "restaurant"
            waiter.start();                                                 //begin execution of the new thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}