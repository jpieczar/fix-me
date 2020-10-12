package com.wtc.utils;

import java.io.*;
import java.net.*;
import java.util.Dictionary;
import java.util.Hashtable;


/**
 * This stores the ids and outputStreams of all the connected clients.
 *
 * The simplest way to assign a unique 6-digit ID is to use the sockets
 * port number. Later, if need be we can create out own ID generation
 * class.
 */
public class IDTable {
    private static IDTable table = null;
    private static int nextID;

    // A dictionary of all clients outputStreams.
    private Dictionary<Integer, OutputStream> clients = new Hashtable<Integer, OutputStream>();

    // Disallow object creation
    private IDTable() {

    }

    public static IDTable getTable(){
        if (table == null){
            table = new IDTable();
        }

        return table;
    }

    /**
     * Add the client to table, and assigns an ID to it.
     *
     * returns the given created ID.
     **/
    public int addNewClient(Socket socket) throws IOException {
        int ID = socket.getPort();
        clients.put(ID, socket.getOutputStream());

        return ID;
    }

    /**
     * Finds the client with the given ID and attempts to send
     * the given message.
     * 
     * If the sending fails, the app should assume that the client has disconnected 
     * from the router and should be removed from the list.
     *
     * TODO: Add error checks for when the outputStream is not valid, or when the user
     * disconnects.
     *
     * TODO:  Whne adding validation and error checks, maybe this could return an 
     * integer representing the operation condition.
     * */
    public void sendMessage(int ID, String message){
        OutputStream outStream = this.clients.get(ID);
        PrintWriter out = new PrintWriter(outStream, true);

        out.println("MESSAGE: " + message);
    }

    /**
     * Removes the client with the given ID from the table.
     * */
    public void removeClient(int ID){
        clients.remove(ID);
    }

}  
