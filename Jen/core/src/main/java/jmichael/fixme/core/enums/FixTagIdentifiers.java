package jmichael.fixme.core.enums;

public enum FixTagIdentifiers {

    ID(0),
    SENDER(1),
    RECIPIENT(2),
    STOCK_ITEM(3),
    QUANTITY(4),
    PRICE(5),
    OPERATION(6),
    RESPONSE(8),
    MESSAGE(9),
    CHECKSUM(10);

    private final int identifier;

    FixTagIdentifiers(int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }
}
