package jmichael.fixme.core;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static jmichael.fixme.core.FixHandler.*;

public class DataHandler {


    private static final String[] STOCK_ITEMS = {
            "Apple", "IBM", "Intel", "Tesla",
            "Twitter", "Microsoft", "Facebook", "Verizon", "Boeing",
            "UnitedHealth", "JPMorgan", "Honeywell", "Chevron"
    };

    public static String readMessage(AsynchronousSocketChannel channel, ByteBuffer readBuffer) {
        try {
            return readDataReceived(channel.read(readBuffer).get(), readBuffer);
        } catch (InterruptedException | ExecutionException e) {
            return "";
        }
    }

    public static String readDataReceived(int bytesRead, ByteBuffer readBuffer) {
        if (bytesRead != -1) {
            readBuffer.flip();
            byte[] bytes = new byte[bytesRead];
            readBuffer.get(bytes, 0, bytesRead);
            readBuffer.clear();
            String message = new String(bytes);
            System.out.println(BRIGHT_GREEN +"[MESSAGE RECEIVED] :: "+BRIGHT_BLUE + message);
            return message;
        }
        return "";
    }

    public static Future<Integer> sendMessage(AsynchronousSocketChannel channel, String message) {
        System.out.println(BRIGHT_GREEN +"[FORWARDING MESSAGE] :: " + BRIGHT_BLUE+message);
        return channel.write(ByteBuffer.wrap(message.getBytes()));
    }

    public static Future<Integer> sendSystemMessage(AsynchronousSocketChannel channel, String message) {
        System.out.println(BRIGHT_GREEN +"[FORWARD TO SYSTEM] :: " + BRIGHT_BLUE+ message);
        final String internalMessage = BRIGHT_GREEN + "[SYSTEM_REPORT] :: "+BRIGHT_BLUE + message;
        return channel.write(ByteBuffer.wrap(internalMessage.getBytes()));
    }

    public static Map<String, Integer> getRandomStock() {
        final Map<String, Integer> stockItems = new HashMap<>();
        final Random random = new Random();
        for(String instrument : STOCK_ITEMS) {
            if (random.nextBoolean()) {
                stockItems.put(instrument, random.nextInt(9) + 1);
            }
        }
        return stockItems;
    }

    public static String getClientName(String[] args) {
        return args.length == 1
                ? args[0]
                : DateTimeFormatter.ofPattern("mmss").format(LocalDateTime.now());
    }
}
