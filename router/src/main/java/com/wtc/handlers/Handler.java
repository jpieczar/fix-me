package com.wtc.handlers;


import java.util.*;
import java.io.*;
import java.net.*;

import com.wtc.utils.*;

/**
 * Task handler for all the clients connecting to the router.
 * 
 * For now this only considers the brokers, Later is should keep in ming
 * that markets and brokers connect from different ports on the router.
 *
 * */
public class Handler implements Runnable {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private IDTable table;
    private int id;

    public Handler(Socket socket){
        this.socket = socket;
        this.table = IDTable.getTable();
    }

    /**
     * After accepting the connection, this will keep listening to the
     * client until it either disconnects or sends a quit operation.
     * 
     *TODO [ ] Add a way to process and validate the syntax of each message.
           [ ] Find  proper way to handle the client disconnecting
           [ ] Figure out how the Chain Of Operation would be used here.
     * */
    @Override
    public void run() {
        try {
            initConnection();

            // get the input stream of the client.
            in = new Scanner(socket.getInputStream());
            
            // Get input until the client disconnects from the server.
            while (true) {
                // get Message
                System.out.println(in.nextLine());

                // send response to the client.
                table.sendMessage(socket.getPort(), "Message recieve.");
            }
        } catch (Exception E) {
            System.out.println(E);
        }
        finally {
            // Remove the socket from the table of clients.
            table.removeClient(socket.getPort());


            //close the socket.
            try{
                socket.close();
            } catch (IOException e){
                System.err.println("Why would this give an error when closing a socket.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Initiates finishes connection with the client by adding them to the table and
     * giving them an ID. 
     * */
    private void initConnection() throws IOException {
        id = table.addNewClient(socket);
        table.sendMessage(id, "CONNECTION ESTABLISHED");
        table.sendMessage(id, "ID: "+ id);

    }
    
}
