package jmichael.fixme.broker;

import jmichael.fixme.broker.handler.ResponseHandler;
import jmichael.fixme.broker.handler.ResponseValidator;
import jmichael.fixme.core.ClientHandler;
import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.exception.MessageValidationException;
import jmichael.fixme.core.messages.MailMan;
import static jmichael.fixme.core.FixHandler.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Broker extends ClientHandler {


    private Broker(String bName) {
        super(5000, "B" + bName);
    }

    private void init() {
        try {
            readFromSocket();

            final Scanner scanner = new Scanner(System.in);
            System.out.println(YELLOW + "[FORWARDING MESSAGE] ::" + CYAN+"[1]ID | [2]Option | [3]Item | [4]Lot size | [5]Price");
            while (true) {
                try {
                    final String message = FixHandler.constructFixMessage(scanner.nextLine(), getId(), getName());
                    final Future<Integer> result = DataHandler.sendMessage(getSocketChannel(), message);
                    result.get();
                } catch (MessageValidationException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    //returns message handler
    protected MailMan getMessageMailman() {
        final MailMan mailMan = super.getMessageMailman();
        final MailMan resultTag = new ResponseValidator();
        final MailMan executionResult = new ResponseHandler();
        mailMan.setNext(resultTag);
        resultTag.setNext(executionResult);
        return mailMan;
    }

    public static void main(String[] args) {
        new Broker(DataHandler.getClientName(args)).init();
    }
}
