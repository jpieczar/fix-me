package jmichael.fixme.broker.handler;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.messages.MainMessageHandler;

import java.nio.channels.AsynchronousSocketChannel;

import static jmichael.fixme.core.FixHandler.BRIGHT_GREEN;

public class ResponseHandler extends MainMessageHandler {

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        final String request = FixHandler.getValueByTag(message, FixTagIdentifiers.RESPONSE);
        final String reqMessage = FixHandler.getValueByTag(message, FixTagIdentifiers.MESSAGE);
        System.out.println(BRIGHT_GREEN + "\n[RESPONSE] " + request + " :: " + reqMessage);
        super.validateMessage(clientChannel, message);
    }
}
