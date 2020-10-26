package jmichael.fixme.core.messages;


import java.nio.channels.AsynchronousSocketChannel;

import static jmichael.fixme.core.FixHandler.BRIGHT_GREEN;

public class SystemMailMan extends MainMessageHandler {

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        if (!message.startsWith(BRIGHT_GREEN+"[SYSTEM_REPORT] :: ")) {
            super.validateMessage(clientChannel, message);
        }
    }
}
