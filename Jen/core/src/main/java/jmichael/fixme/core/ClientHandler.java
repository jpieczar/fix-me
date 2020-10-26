package jmichael.fixme.core;

import jmichael.fixme.core.validators.ChecksumChecker;
import jmichael.fixme.core.messages.SystemMailMan;
import jmichael.fixme.core.validators.AllFixTagsValidator;
import jmichael.fixme.core.messages.MailMan;
import static jmichael.fixme.core.FixHandler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class ClientHandler {

    private static final String DEFAULT_ID = "000000";

    private final ByteBuffer buffer = ByteBuffer.allocate(FixHandler.BUFFER_SIZE);
    private final int port;
    private final String name;

    private AsynchronousSocketChannel socketChannel;
    private String id = DEFAULT_ID;

    public ClientHandler(int port, String name) {
        this.port = port;
        this.name = name;
    }

    protected AsynchronousSocketChannel getSocketChannel() {
        if (socketChannel == null) {
            socketChannel = connectToMessageRouter();
            DataHandler.sendMessage(socketChannel, name);
            id = DataHandler.readMessage(socketChannel, buffer);
            System.out.println(YELLOW + name + " ID :: " + BRIGHT_CYAN +id);
            return socketChannel;
        }
        return socketChannel;
    }

    private AsynchronousSocketChannel connectToMessageRouter() {
        final AsynchronousSocketChannel socketChannel;
        try {
            socketChannel = AsynchronousSocketChannel.open();
            final Future future = socketChannel.connect(new InetSocketAddress(FixHandler.HOST, port));
            future.get();
        } catch (IOException | InterruptedException | ExecutionException e) {
            System.out.println(RED+"[ERROR] :: Could not connect to Message Router, reconnecting...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            return connectToMessageRouter();
        }
        return socketChannel;
    }

    private void invalidateConnection() {
        socketChannel = null;
    }

    protected String getId() {
        return id;
    }

    protected String getName() {
        return name;
    }

    protected void readFromSocket() {
        getSocketChannel().read(buffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                final String message = DataHandler.readDataReceived(result, buffer);
                if (!"".equals(message)) {
                    onSuccessRead(message);
                    getSocketChannel().read(buffer, null, this);
                } else {
                    reconnect();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                reconnect();
            }

            private void reconnect() {
                System.out.println(RED+"[WARNING] Connection to router has ended. Please reconnect!");
                invalidateConnection();
                getSocketChannel().read(buffer, null, this);
            }
        });
    }

    private void onSuccessRead(String message) {
        getMessageMailman().validateMessage(getSocketChannel(), message);
    }

    protected MailMan getMessageMailman() {
        final MailMan mailMan = new SystemMailMan();
        final MailMan mandatoryTagsValidator = new AllFixTagsValidator();
        final MailMan checksumValidator = new ChecksumChecker();
        mailMan.setNext(mandatoryTagsValidator);
        mandatoryTagsValidator.setNext(checksumValidator);
        return mailMan;
    }
}
