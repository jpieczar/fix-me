package jmichael.fixme.core.messages;

import java.nio.channels.AsynchronousSocketChannel;

public abstract class MainMessageHandler implements MailMan {

    private MailMan next;
    @Override
    public final void setNext(MailMan next) {
        this.next = next;
    }

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        if (next != null) {
            next.validateMessage(clientChannel, message);
        }
    }
}
