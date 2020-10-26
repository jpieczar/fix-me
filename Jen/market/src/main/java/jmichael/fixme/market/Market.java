package jmichael.fixme.market;

import jmichael.fixme.core.ClientHandler;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.messages.MailMan;
import jmichael.fixme.market.handler.FixTagValidator;
import jmichael.fixme.market.handler.MessageIDExecutor;
import static jmichael.fixme.core.FixHandler.*;
import java.util.Map;

public class Market extends ClientHandler {


    private final Map<String, Integer> stockItems;

    private Market(String name) {
        super(5001, "M" + name);
        stockItems = DataHandler.getRandomStock();
    }

    private void runMarket() {
        String item_list = stockItems.toString().replaceAll(", ", "|");
        System.out.println(BRIGHT_BLUE + "[Stock Items] :: " + item_list.replace("{", "[").replace("}", "]"));
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
