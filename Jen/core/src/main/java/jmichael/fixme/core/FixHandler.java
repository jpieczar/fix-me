package jmichael.fixme.core;

import jmichael.fixme.core.enums.FixTagIdentifiers;
import jmichael.fixme.core.enums.BrokerResponse;
import jmichael.fixme.core.exception.MessageValidationException;
import jmichael.fixme.core.exception.FixTagException;

import java.util.regex.Pattern;

public class FixHandler {

    public static final String RED = "\u001B[31m";
    public static final String BRIGHT_BLUE   = "\u001B[36m";
    public static final String BRIGHT_GREEN  = "\u001B[92m";
    public static final String CYAN = "\u001B[36m";
    public static final String RESET  = "\u001B[0m";
    public static final String BRIGHT_BG_WHITE  = "\u001B[107m";
    public static final String YELLOW = "\u001B[92m";
    public static final String BRIGHT_CYAN   = "\u001B[36m";

    public static final String HOST = "127.0.0.1";
    public static final String ID_FORMAT = "%06d";
    public static final int BUFFER_SIZE = 4096;

    public static String constructFixMessage(String input, String id, String name) throws MessageValidationException {
        final String[] m = input.split(" ");
        if (m.length != 5) {
            throw new MessageValidationException(RED+ "\n[ERROR] :: Invalid input. Please enter 5 args \n"+BRIGHT_BLUE+"[1]ID | [2]Option | [3]Item | [4]Lot size | [5]Price");
        }
        final StringBuilder builder = new StringBuilder();
        addTag(builder, FixTagIdentifiers.ID, id);
        addTag(builder, FixTagIdentifiers.SENDER, name);
        addTag(builder, FixTagIdentifiers.RECIPIENT, m[0]);
        addTag(builder, FixTagIdentifiers.OPERATION, m[1]);
        addTag(builder, FixTagIdentifiers.STOCK_ITEM, m[2]);
        addTag(builder, FixTagIdentifiers.QUANTITY, m[3]);
        addTag(builder, FixTagIdentifiers.PRICE, m[4]);
        addTag(builder, FixTagIdentifiers.CHECKSUM, checksumCalculator(builder.toString()));
        return builder.toString();
    }

    public static String responseFixMessage(String message, String id, String srcName, String targetName, BrokerResponse result) {
        final StringBuilder builder = new StringBuilder();
        addTag(builder, FixTagIdentifiers.ID, id);
        addTag(builder, FixTagIdentifiers.SENDER, srcName);
        addTag(builder, FixTagIdentifiers.RECIPIENT, targetName);
        addTag(builder, FixTagIdentifiers.RESPONSE, result.toString());
        addTag(builder, FixTagIdentifiers.MESSAGE, message);
        addTag(builder, FixTagIdentifiers.CHECKSUM, checksumCalculator(builder.toString()));
        return builder.toString();
    }

    private static void addTag(StringBuilder builder, FixTagIdentifiers tag, String value) {
        builder.append(tag.getIdentifier())
                .append("=")
                .append(value)
                .append("|");
    }

    public static String checksumCalculator(String message) {
        final byte[] bytes = message.getBytes();
        int sum = 0;
        for (byte aByte : bytes) {
            sum += aByte;
        }
        return String.format("%03d", sum % 256);
    }

    public static String getMessageWithoutChecksum(String fixMessage) {
        final int checksumIndex = fixMessage.lastIndexOf(FixTagIdentifiers.CHECKSUM.getIdentifier() + "=");
        return fixMessage.substring(0, checksumIndex);
    }

    public static String getValueByTag(String fixMessage, FixTagIdentifiers tag) {
        final String[] tagValues = fixMessage.split(Pattern.quote("|"));
        final String searchPattern = tag.getIdentifier() + "=";
        for (String tagValue : tagValues) {
            if (tagValue.startsWith(searchPattern)) {
                return tagValue.substring(searchPattern.length());
            }
        }
        throw new FixTagException(RED +"[ERROR] :: No '" + tag + "' tag in message + '" + fixMessage + "'");
    }
}
