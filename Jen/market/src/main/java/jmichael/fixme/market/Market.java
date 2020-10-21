package jmichael.fixme.market;

import jmichael.fixme.core.ClientHandler;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.messages.MailMan;
import jmichael.fixme.market.handler.FixTagValidator;
import jmichael.fixme.market.handler.MessageIDExecutor;

import java.util.Map;

public class Market extends ClientHandler {


    public static final String BRIGHT_BLUE   = "\u001B[36m";
    public static final String BRIGHT_GREEN  = "\u001B[92m";
    public static final String RESET  = "\u001B[0m";
    private final Map<String, Integer> stockItems;

    private Market(String name) {
        super(5001, "M" + name);
        stockItems = DataHandler.getRandomStock();
    }

    private void runMarket() {
        System.out.println(BRIGHT_BLUE + "[Stock Items] :: " + stockItems.toString() + "\n");
        System.out.println(RESET);
        readFromSocket();

        while (true) ;
    }

    @Override
    protected MailMan getMessageMailman() {
        final MailMan mailMan = super.getMessageMailman();
        final MailMan tagsValidator = new FixTagValidator(getId(), getName());
        final MailMan messageExecutor = new MessageIDExecutor(getId(), getName(), stockItems);
        mailMan.setNext(tagsValidator);
        tagsValidator.setNext(messageExecutor);
        return mailMan;
    }

    public static void main(String[] args) {
        new Market(DataHandler.getClientName(args)).runMarket();
    }
}
