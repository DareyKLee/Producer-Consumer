import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This application organizes a dialog with the user who chooses the parameters that control a simulation of events
 * that occur inside a restaurant. It also handles the interactions that customers make with the restaurant staff
 * members (client side processing of consumer threads).
 *
 * @author Darey Lee
 */
public class Main {
    static InetAddress host;
    static final int port = 9999;

    /**
     * main() is responsible for finding the host IP, creating a new instance of UserSettings, and calling initialize()
     */
    public static void main(String[] args) {
        try {
            host = InetAddress.getLocalHost();

        } catch (UnknownHostException e) {
            System.out.println("Cannot find host address");
            System.exit(1);
        }

        UserSettings settings = new UserSettings();     //create a new UserSettings object which will be sent to server

        initialize(settings);
    }

    /**
     * initialize() is responsible for calling contactServer() and starting new customer threads
     * @param settings is an UserSettings object containing the values the simulation runs with
     */
    public static void initialize(UserSettings settings){
        contactServer(settings);

        for (int i = 0; i < settings.getNumCustomers(); i++){   //utilize a for loop to spin up the specified
            new CustomerThread(i + 1).start();          //amount of customer threads
        }
    }

    /**
     * contactServer() is responsible for making initial contact with the server, sending an UserSettings object to the
     * server, and waiting for a server response before the closing the socket used to send the UserSettings object
     * @param settings is an UserSettings object containing the values the simulation runs with
     */
    public static void contactServer(UserSettings settings){
        Socket socket = null;

        try{
            socket = new Socket(host, port);    //direct the socket to the host IP and port number

            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                            socket.getInputStream()));

            ObjectOutputStream out = new ObjectOutputStream(            //create a new ObjectOutputStream to send
                                        socket.getOutputStream());      //UserSettings objects
            String response;

            System.out.println("Attempting to contact server with settings...\n");

            out.writeObject(settings);  //send an UserSettings object named "settings" to the server

            response = in.readLine();                           //wait for and read the next server response
            System.out.println("SERVER> " + response + "\n");   //then display server response

        } catch (IOException IOEX) {
            IOEX.printStackTrace();

        } finally {     //close the socket used for sending UserSettings objects since it is no longer required
            try {
                System.out.println("**CLOSING SOCKET DESIGNATED FOR SENDING PARAMETERS**\n");
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
