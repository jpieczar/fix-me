package router;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import router.utils.*;
import router.handlers.*;

/**
 * A simple router version... It will work with just acception connections,
 * generating IDs, and  forwarding messages.
 */
public class Router {
    public static void main(String[] args) throws IOException{
        System.out.println("The server is running");

        var pool = Executors.newFixedThreadPool(50);

        try (var listener = new ServerSocket(5000)){
            while(true){
                // accept connection.
                var socket = listener.accept();
                // This will move to the handler.
                System.out.println("id: " + IDTable.getTable().addNewClient(socket));
                IDTable.getTable().sendMessage(socket.getPort(), "Here is your id " + 1);

                pool.execute(new Handler(socket));

            }
        }

    }

}