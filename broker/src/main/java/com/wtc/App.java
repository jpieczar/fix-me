package com.wtc;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * The broker client, is the one making requests to the market client with 
 * buy or sell messages.
 *
 * For now it just establishes a connection, gets the id, and send all the lines the user types
 * to the server.
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument.");
            return;
        }

        try (var socket = new Socket(args[0], 5000)) {
            System.out.println("Press CTRL-D to stop close the connection.");
            var scanner = new Scanner(System.in);
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
            
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
                System.out.println(in.nextLine());
            }
            out.println(scanner.nextLine());
            System.out.println(in.nextLine());
        }
    }
}
