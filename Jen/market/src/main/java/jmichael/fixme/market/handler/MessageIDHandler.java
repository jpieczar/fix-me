package jmichael.fixme.market.handler;

import jmichael.fixme.core.FixHandler;
import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.enums.BrokerResponse;
import jmichael.fixme.core.DataHandler;
import jmichael.fixme.core.database.SqliteDB;
import jmichael.fixme.core.messages.MainMessageHandler;

import java.nio.channels.AsynchronousSocketChannel;

public abstract class MessageIDHandler extends MainMessageHandler {

    private final String id;
    private final String name;

    public MessageIDHandler(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected void rejectedMessage(AsynchronousSocketChannel clientChannel, String fixMessage, String message) {
        sendMessage(clientChannel, fixMessage, message, BrokerResponse.Rejected);
    }

    protected void executedMessage(AsynchronousSocketChannel clientChannel, String fixMessage, String message) {
        sendMessage(clientChannel, fixMessage, message, BrokerResponse.Accepted);
    }

    private void sendMessage(AsynchronousSocketChannel clientChannel, String fixMessage, String message, BrokerResponse response) {
        final String targetName = FixHandler.getValueByTag(fixMessage, FixTagIdentifiers.SENDER);
        if (isInsertMessagesToDb()) {
            SqliteDB.insertData(
                    name,
                    targetName,
                    FixHandler.getValueByTag(fixMessage, FixTagIdentifiers.OPERATION),
                    FixHandler.getValueByTag(fixMessage, FixTagIdentifiers.STOCK_ITEM),
                    FixHandler.getValueByTag(fixMessage, FixTagIdentifiers.PRICE),
                    FixHandler.getValueByTag(fixMessage, FixTagIdentifiers.QUANTITY),
                    response.toString(),
                    message);
            SqliteDB.selectAll();
        }
        DataHandler.sendMessage(clientChannel, FixHandler.responseFixMessage(message, id, name, targetName, response));
    }

    protected boolean isInsertMessagesToDb() {
        return false;
    }
}
