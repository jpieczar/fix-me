package router.handlers;

import java.util.*;
import java.net.*;
import java.io.*;
import router.utils.*;

/**
 * This is a task handler for all clients...
 * It is the main method after the client has connected.
 */
public class Handler implements Runnable {
    private Socket socket;
    private Scanner in ;
    private PrintWriter out;

    public Handler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        // Create the clients ID
        try {
            in = new Scanner(socket.getInputStream());
            
            // Get input from the client until they disconnect.
            while(true){
                // get the clients message
                System.out.println(in.nextLine());

                // send the client message back.
                IDTable.getTable().sendMessage(socket.getPort(), "The message was recieved.");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        finally {
            // remove socket reference from the table.
            IDTable.getTable().removeClient(socket.getPort()); 
            // close the socket.
            try{
                socket.close();
            }catch(IOException e)
            {
               //print freindly message. 
            }
        }
    }
}
