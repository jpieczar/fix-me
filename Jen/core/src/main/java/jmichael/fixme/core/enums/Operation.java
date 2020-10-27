package jmichael.fixme.core.enums;

public enum Operation {
    Buy,
    Sell;

    public static boolean isOption(String type) {
        return type.equals(Buy.toString()) || type.equals(Sell.toString());
    }
}
