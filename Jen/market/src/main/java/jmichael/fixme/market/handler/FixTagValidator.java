package jmichael.fixme.market.handler;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.enums.Operation;
import jmichael.fixme.core.exception.FixTagException;

import java.nio.channels.AsynchronousSocketChannel;
import static jmichael.fixme.core.FixHandler.*;

public class FixTagValidator extends MessageIDHandler {

    public FixTagValidator(String id, String name) {
        super(id, name);
    }

    @Override
    public void validateMessage(AsynchronousSocketChannel clientChannel, String message) {
        try {
            FixHandler.getValueByTag(message, FixTagIdentifiers.STOCK_ITEM);
            final int price = Integer.parseInt(FixHandler.getValueByTag(message, FixTagIdentifiers.PRICE));
            final int quantity = Integer.parseInt(FixHandler.getValueByTag(message, FixTagIdentifiers.QUANTITY));
            if (quantity <= 0 || quantity > 10000) {
                rejectedMessage(clientChannel, message, RED+"\n[ERROR] :: Quantity out of range [1 - 10 000] ");
                return;
            } else if (price <= 0 || price > 10000) {
                rejectedMessage(clientChannel, message, RED+"\n[ERROR] :: Price out of range [1 - 10 000] ");
                return;
            }

            final String type = FixHandler.getValueByTag(message, FixTagIdentifiers.OPERATION);
            if (Operation.isOption(type)) {
                super.validateMessage(clientChannel, message);
            } else {
                rejectedMessage(clientChannel, message, RED+"\n[ERROR] :: Invalid Operation [Buy/Sell] ");
            }
        } catch (FixTagException ex) {
            rejectedMessage(clientChannel, message, RED+"\n[ERROR] :: Invalid FIX Tags ");
        } catch (NumberFormatException ex) {
            rejectedMessage(clientChannel, message, RED+"\n[ERROR] :: Invalid Value ");
        }
    }
}
