package jmichael.fixme.core.validators;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.exception.FixTagException;
import jmichael.fixme.core.messages.MainMessageHandler;

import java.nio.channels.AsynchronousSocketChannel;

import static jmichael.fixme.core.FixHandler.RED;

public class AllFixTagsValidator extends MainMessageHandler {


    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        try {
            final String sourceId = FixHandler.getValueByTag(message, FixTagIdentifiers.ID);
            FixHandler.getValueByTag(message, FixTagIdentifiers.SENDER);
            FixHandler.getValueByTag(message, FixTagIdentifiers.RECIPIENT);
            final String checksum = FixHandler.getValueByTag(message, FixTagIdentifiers.CHECKSUM);

            Integer.parseInt(sourceId);
            Integer.parseInt(checksum);
            super.validateMessage(clientChannel, message);
        } catch (FixTagException ex) {
            DataHandler.sendSystemMessage(clientChannel, ex.getMessage());
        } catch (NumberFormatException ex) {
            DataHandler.sendSystemMessage(clientChannel, RED+"ID and checksum tags should be numbers :: " + message);
        }
    }
}
