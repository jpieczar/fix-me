package jmichael.fixme.core.validators;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.messages.MainMessageHandler;

import java.nio.channels.AsynchronousSocketChannel;

import static jmichael.fixme.core.FixHandler.*;

public class ChecksumChecker extends MainMessageHandler {

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        final String calculatedChecksum = FixHandler.checksumCalculator(FixHandler.getMessageWithoutChecksum(message));
        final String messageChecksum = FixHandler.getValueByTag(message, FixTagIdentifiers.CHECKSUM);
        final boolean isValidChecksum = calculatedChecksum.equals(messageChecksum);
        if (isValidChecksum) {
            super.validateMessage(clientChannel, message);
            System.out.println(BRIGHT_GREEN+"Calculated Checksum :: " + calculatedChecksum);
            System.out.println(BRIGHT_GREEN+"Message Checksum :: " + calculatedChecksum);
        } else {
            DataHandler.sendSystemMessage(clientChannel, RED+"[ERROR] :: Invalid checksum for message " + message);
        }
    }
}
