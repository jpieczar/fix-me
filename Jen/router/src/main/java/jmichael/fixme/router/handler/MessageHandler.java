package jmichael.fixme.router.handler;

import jmichael.fixme.core.FixHandler;
import static jmichael.fixme.core.FixHandler.*;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.messages.MainMessageHandler;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;


public class MessageHandler extends MainMessageHandler {


    private final Map<String, AsynchronousSocketChannel> routingTable;
    private final Map<String, String> failedMessages;

    public MessageHandler(Map<String, AsynchronousSocketChannel> routingTable,
                          Map<String, String> failedMessages) {
        this.routingTable = routingTable;
        this.failedMessages = failedMessages;
    }

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        System.out.println(BRIGHT_GREEN + "\n[VALIDATING INPUT] :: " + BRIGHT_BLUE + message);
        final String targetName = FixHandler.getValueByTag(message, FixTagIdentifiers.RECIPIENT);
        final AsynchronousSocketChannel targetChannel = routingTable.get(targetName);
        if (targetChannel != null) {
            DataHandler.sendMessage(targetChannel, message);
            super.validateMessage(clientChannel, message);
        } else {
            DataHandler.sendSystemMessage(clientChannel,
                    RED+ "\n[ERROR] :: No id found that matches " + targetName + "\n" + "[RESENDING]...");
            failedMessages.put(targetName, message);
        }
    }
}
