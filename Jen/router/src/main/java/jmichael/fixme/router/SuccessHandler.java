package jmichael.fixme.router;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.messages.MailMan;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static jmichael.fixme.core.FixHandler.*;

class SuccessHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final AsynchronousServerSocketChannel clientListener;
    private final Map<String, AsynchronousSocketChannel> routingTable;
    private final AtomicInteger id;
    private final MailMan mailMan;

    private String clientName = "clientName";

    SuccessHandler(AsynchronousServerSocketChannel clientListener, Map<String, AsynchronousSocketChannel> routingTable,
                   AtomicInteger id, MailMan mailMan) {
        this.clientListener = clientListener;
        this.routingTable = routingTable;
        this.id = id;
        this.mailMan = mailMan;
    }

    @Override
    public void completed(AsynchronousSocketChannel channel, Object attachment) {
        clientListener.accept(null, this);
        final ByteBuffer buffer = ByteBuffer.allocate(FixHandler.BUFFER_SIZE);
        clientName = DataHandler.readMessage(channel, buffer);

        sendClientId(channel, getNextId());

        while (true) {
            final String message = DataHandler.readMessage(channel, buffer);
            if ("".equals(message)) {
                break;
            }
            executor.execute(() -> mailMan.validateMessage(channel, message));
       //     System.out.println("TESTING");
        }
        closeConnection();
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        closeConnection();
    }

    private void sendClientId(AsynchronousSocketChannel channel, String currentId) {
        System.out.println();
        System.out.println(BRIGHT_GREEN + "Status :: "+ BRIGHT_BLUE+ clientName + " [CONNECTED]\n" +BRIGHT_GREEN +"ID :: " +CYAN + currentId);
        DataHandler.sendMessage(channel, currentId);
        routingTable.put(clientName, channel);
        getTable();
    }

    private void closeConnection() {
        routingTable.remove(clientName);
        System.out.println();
        System.out.println(RED + " [CONNECTION CLOSED] :: " + clientName );
        getTable();
    }

    private void getTable() {
        System.out.println("\n"+BRIGHT_BLUE+ "Routing Table :: " + routingTable.keySet().toString());
        System.out.println(RESET);
    }

    private String getNextId() {
        return String.format(FixHandler.ID_FORMAT, id.getAndIncrement());
    }
}
