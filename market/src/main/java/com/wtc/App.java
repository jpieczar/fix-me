package com.wtc;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * The market client, is the one making requests to the market client with 
 * buy or sell messages.
 *
 * For now it just establishes a connection, gets the id, and send all the lines the user types
 * to the server.
 *
 * Maybe this could be changed into a class so that I can have a factory for each broker...
 * Just thinking.
 */
public class App 
{
    private static int assignedID = 0;
    private static PrintWriter out;
    private static Scanner in;


    public static void main( String[] args ) throws IOException {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument.");
            return;
        }

        try (var socket = new Socket(args[0], 5000)) {
            System.out.println("Press CTRL-D to stop close the connection.");
            var scanner = new Scanner(System.in);

            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            // Start get the connection ID.
            startConnection(socket);
            
            // Send and recieve information.
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
                System.out.println(in.nextLine());
            }
        }
    }

    /**
     * Starts a connection with the router.
     *
     * For now it just prints the connection information recieved from the server.
     *
     * TODO Make it return a socket object that will only after a successfull connection was
     * made.
     *
     * TODO Error check the connection status.*/
    private static void startConnection(Socket socket){
        String connectionStatus;
        String connectionID;

        // Check the status of the connection
        connectionStatus = in.nextLine();
        System.out.println(connectionStatus);

        // get the connection ID for the current ssession.
        connectionID = in.nextLine();
        System.out.println(connectionID);
    }
}
