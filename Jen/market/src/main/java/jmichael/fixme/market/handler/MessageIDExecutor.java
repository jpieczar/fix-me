package jmichael.fixme.market.handler;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.enums.Operation;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;

public class MessageIDExecutor extends MessageIDHandler {

    private final Map<String, Integer> stockItems;

    String RED = "\u001B[31m";
    public static final String BRIGHT_BLUE   = "\u001B[36m";
    public static final String BRIGHT_GREEN  = "\u001B[92m";

    public MessageIDExecutor(String clientId, String name, Map<String, Integer> stockItems) {
        super(clientId, name);
        this.stockItems = stockItems;
    }

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        final String stockItem = FixHandler.getValueByTag(message, FixTagIdentifiers.STOCK_ITEM);
        if (stockItems.containsKey(stockItem)) {
            final int quantity = Integer.parseInt(FixHandler.getValueByTag(message, FixTagIdentifiers.QUANTITY));
            final int marketQuantity = stockItems.get(stockItem);
            final String type = FixHandler.getValueByTag(message, FixTagIdentifiers.OPERATION);
            if (type.equals(Operation.Buy.toString())) {
                if (marketQuantity < quantity) {
                    rejectedMessage(clientChannel, message, RED+"Not enough stock");
                    return;
                } else {
                    stockItems.put(stockItem, marketQuantity - quantity);
                }
            } else {
                stockItems.put(stockItem, marketQuantity + quantity);
            }
            System.out.println(BRIGHT_BLUE +"[STOCK] :: " + stockItems.toString());
            executedMessage(clientChannel, message, BRIGHT_GREEN+"[OK]");
        } else {
            rejectedMessage(clientChannel, message, RED+"\n[ERROR] :: " + stockItem + " Chosen stock is not traded on this market");
        }
    }

    @Override
    protected boolean isInsertMessagesToDb() {
        return true;
    }
}
