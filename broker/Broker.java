package broker;


import java.util.*;
import java.net.*;
import java.io.*;


/**
 * This is one of the clients for the whole system.
 * 
 * For now it just parrots what the server is sending.
 */
public class Broker {
    public static void main(String[] args) throws IOException {
        if (args.length != 1){
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }

        try (var socket = new Socket(args[0], 5000)){
            System.out.println("Send random messages, CTRL-D to stop everythin");
            var scanner = new Scanner(System.in);
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);

            while (scanner.hasNextLine()){
                out.println(scanner.nextLine());
                System.out.println(in.nextLine());
            }
        }


    }
    
}
