package router.utils;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * This is where the ids and the message streams should be Hosted.
 * 
 * For now I will not think about thread safety.
 */
public class IDTable {
    // There should only be one table for all tasks.
    private static IDTable table = null;
    private static int nextID = 0;

    // A dictionary of all the clients ids and their outputStreams.
    Dictionary<Integer, OutputStream> clients = new Hashtable<Integer, OutputStream>();

    private IDTable(){

    }

    public static IDTable getTable(){
        if(table == null){
            table = new IDTable();
        }

        return table;
    }

    /** This is where a client is added and given the id
     * It returns the id used for this specific socket.
     */
    public int addNewClient(Socket socket) throws IOException{
        clients.put(socket.getPort(), socket.getOutputStream());
        return socket.getPort();
    }

    /**
     * This function should just find the give id and send a message to it.
     */
    public void sendMessage(int ID, String message){
        var outStream = clients.get(ID);
        var out = new PrintWriter(outStream, true);
        out.println("THis is sent from the helper: " + message);
    }

    public void removeClient(int ID){
        clients.remove(ID);
    }

}
