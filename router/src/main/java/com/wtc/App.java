package com.wtc;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;

import com.wtc.handlers.Handler;


/**
 * The router simple accepts connections from clients and forwards messages
 * between all the clients.
 *
 * It uses ID's to identify a client to send messages to.
 *
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException{
        System.out.println("The router is running.");

        var pool = Executors.newFixedThreadPool(50);

        try (var listener = new ServerSocket(5000)){
            while (true) {

                var socket = listener.accept();

                pool.execute(new Handler(socket));
            }
        }

    }
}
