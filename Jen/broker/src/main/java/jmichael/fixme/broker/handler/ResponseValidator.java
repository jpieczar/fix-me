package jmichael.fixme.broker.handler;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.enums.BrokerResponse;
import jmichael.fixme.core.exception.FixTagException;
import jmichael.fixme.core.messages.MainMessageHandler;

import java.nio.channels.AsynchronousSocketChannel;

public class ResponseValidator extends MainMessageHandler {

    String RED = "\u001B[31m";

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        final String response;
        try {
            response = FixHandler.getValueByTag(message, FixTagIdentifiers.RESPONSE);
        } catch (FixTagException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (BrokerResponse.is(response)) {
            super.validateMessage(clientChannel, message);
        } else {
            System.out.println(RED + "Request rejected -" + message +"\n");
        }
    }
}
