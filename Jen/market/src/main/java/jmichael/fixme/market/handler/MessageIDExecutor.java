package jmichael.fixme.market.handler;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.enums.Operation;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import static jmichael.fixme.core.FixHandler.*;

public class MessageIDExecutor extends MessageIDHandler {

    private final Map<String, Integer> stockItems;

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
                    rejectedMessage(clientChannel, message, "Not enough stock");
                    return;
                } else {
                    stockItems.put(stockItem, marketQuantity - quantity);
                }
            } else {
                stockItems.put(stockItem, marketQuantity + quantity);
            }

            String items_list = stockItems.toString().replaceAll(", ", "|");
            System.out.println(BRIGHT_BLUE +"[STOCK] :: " + items_list.replace("{", "[").replace("}", "]"));
            executedMessage(clientChannel, message,"[OK]");
        } else {
            rejectedMessage(clientChannel, message, "Invalid stock");
        }
    }

    @Override
    protected boolean isInsertMessagesToDb() {
        return true;
    }
}
