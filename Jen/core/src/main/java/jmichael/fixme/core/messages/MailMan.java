package jmichael.fixme.core.messages;

import java.nio.channels.AsynchronousSocketChannel;

public interface MailMan {

    void setNext(MailMan handler);

    void validateMessage(AsynchronousSocketChannel clientChannel, String message);
}
