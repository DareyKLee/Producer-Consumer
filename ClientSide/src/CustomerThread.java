import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * CustomerThread is the class to create the consumers in the system which makes requests to the server in order to
 * consume items in the buffer
 */
public class CustomerThread extends Thread {
    private String name;            //Each unique CustomerThread is given a number based on the order it was spawned
    private int foodCount = 0;      //and personal counter that keeps track of how much it has consumed

    /**
     * The constructor of CustomerThread.
     * @param number is the numerical order it is generated in inside a for loop
     */
    public CustomerThread(int number){
        this.name = "Customer #" + number;
    }

    /**
     * Customers continuously sends requests to the server to consume an item in the buffer, waits for a server
     * response for each request, incrementing foodCount upon a fulfilled request, and waiting between 2 to 5 seconds
     * before repeating these steps. If the server responds with "NO MORE FOOD" however, the customer will display
     * foodCount before closing the socket to the server and finish its execution.
     */
    @Override
    public void run(){
        Socket socket = null;
        String message;

        try {
            socket = new Socket(Main.host, Main.port);  //direct socket to the host IP and port number specified in Main

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            do {
                System.out.println(name + " MAKES REQUEST");

                out.println(name);          //send the name of the CustomerThread to the server, this is a request to
                                            //consume an item from the buffer

                message = in.readLine();    //wait for and read the next server response

                if (message.equals("NO MORE FOOD")){    //if the response is "NO MORE FOOD", trigger break to exit
                    break;                              //the do while true loop to complete execution
                }

                System.out.println(message);

                foodCount++;        //increment foodCount which represents the number of items a CustomerThread consumed

                sleep((int) (Math.random() * 2000) + 3000);     //CustomerThreads pause between 3 to 5 seconds
                                                                      //before they can request for more food
            } while (true);

            System.out.println(name + " ATE " + foodCount + " units of food.");     //display the amount of items
                                                                                    //consumed upon leaving the loop
        } catch (InterruptedException interruptedEX) {
            interruptedEX.printStackTrace();

        } catch (IOException IOEX) {
            IOEX.printStackTrace();

        } finally {    //close the socket used for sending consume requests to the server since it is no longer required
            try {
                System.out.println(name + " NOW LEAVING THE RESTAURANT (**CLOSING THREAD SOCKET**).");
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}