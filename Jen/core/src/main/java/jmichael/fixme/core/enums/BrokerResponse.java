package jmichael.fixme.core.enums;

public enum BrokerResponse {
    Accepted,
    Rejected;

    public static boolean is(String response) {
        return response.equals(Accepted.toString()) || response.equals(Rejected.toString());
    }
}
