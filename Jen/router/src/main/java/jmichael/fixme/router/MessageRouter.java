package jmichael.fixme.router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import jmichael.fixme.core.FixHandler;
import static jmichael.fixme.core.FixHandler.*;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.messages.SystemMailMan;
import jmichael.fixme.core.validators.AllFixTagsValidator;
import jmichael.fixme.core.messages.MailMan;
import jmichael.fixme.core.validators.ChecksumChecker;
import jmichael.fixme.router.handler.MessageHandler;


public class MessageRouter {


    private final AtomicInteger id = new AtomicInteger(1);
    private final Map<String, AsynchronousSocketChannel> routingTable = new ConcurrentHashMap<>();
    private final Map<String, String> failedToSendMessages = new ConcurrentHashMap<>();

    private void runRouter() {
        System.out.println(BRIGHT_BLUE+BRIGHT_BG_WHITE +"Message Router Status   ::" + " [ON]");
        System.out.println(BRIGHT_BLUE+BRIGHT_BG_WHITE +"Market Server port      ::" + " [5001]");
        System.out.println(BRIGHT_BLUE+BRIGHT_BG_WHITE +"Broker Server port      ::" + " [5000]");
        System.out.print(RESET);

        try {
            final MailMan mailMan = getMessageHandler();

            final AsynchronousServerSocketChannel brokersListener = AsynchronousServerSocketChannel
                    .open()
                    .bind(new InetSocketAddress(FixHandler.HOST, 5000));
            brokersListener.accept(null,
                    new SuccessHandler(brokersListener, routingTable, id, mailMan));

            final AsynchronousServerSocketChannel marketsListener = AsynchronousServerSocketChannel
                    .open()
                    .bind(new InetSocketAddress(FixHandler.HOST, 5001));
            marketsListener.accept(null,
                    new SuccessHandler(marketsListener, routingTable, id, mailMan));
        } catch (IOException e) {
            System.out.println(RED + "\n[ERROR] :: Socket was not opened.");
        }
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
            tryToSendFailedMessages();
        }
    }

    private void tryToSendFailedMessages() {
        if (!failedToSendMessages.isEmpty()) {
            System.out.println(RED + "[RESENDING MESSAGES] ...");
            failedToSendMessages.keySet().removeIf(targetName -> {
                final AsynchronousSocketChannel targetChannel = routingTable.get(targetName);
                if (targetChannel != null) {
                    System.out.println(BRIGHT_GREEN+ " [Message] " + targetName + " - [SENDING]");
                    System.out.print(RESET);
                    DataHandler.sendMessage(targetChannel, failedToSendMessages.get(targetName));
                    return true;
                }
                return false;
            });
        }
    }

    private MailMan getMessageHandler() {
        final MailMan mailMan = new SystemMailMan();
        final MailMan mandatoryTagsValidator = new AllFixTagsValidator();
        final MailMan checksumValidator = new ChecksumChecker();
        final MailMan messageParser = new MessageHandler(routingTable, failedToSendMessages);
        mailMan.setNext(mandatoryTagsValidator);
        mandatoryTagsValidator.setNext(checksumValidator);
        checksumValidator.setNext(messageParser);
        return mailMan;
    }

    public static void main(String[] args) {
        new MessageRouter().runRouter();
    }
}
