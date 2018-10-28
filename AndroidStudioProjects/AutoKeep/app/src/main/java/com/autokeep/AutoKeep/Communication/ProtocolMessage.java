package com.autokeep.AutoKeep.Communication;

public enum ProtocolMessage {
    OK,
    WRONG_CREDENTIAL,
    LOGIN,
    USER_IS_BANNED,
    SEARCH_VEHICLE,
    NO_AVAILABLE_VEHICLES,
    NEW_ORDER,
    VIEW_ORDERS,
    USER_CHANGE_PASSWORD,
    USER_MODEL,
    RESERVATION_MODEL,
    VEHICLE_MODEL,
    USER_MODEL_LIST,
    RESERVATION_MODEL_LIST,
    VEHICLE_MODEL_LIST,
    //Error Messages - Except ERROR all other error used for messages only
    ERROR,
    TOO_MANY_AUTHENTICATION_RETRIES,
    INTERNAL_ERROR;

    public static String getMessage(ProtocolMessage protocolMessage, String... customMessage) {
        String messageString = null;

        switch (protocolMessage) {
            case OK:
                messageString = "OK";
                break;

            case WRONG_CREDENTIAL:
                messageString = "Email or password is incorrect";
                break;

            case TOO_MANY_AUTHENTICATION_RETRIES:
                messageString = "To many login request retries !";
                break;

            case NO_AVAILABLE_VEHICLES:
                messageString = "There is no available vehicles for correct parameters";
                break;

            case INTERNAL_ERROR:
                messageString = "Internal error.\nPlease contact suppport";
                break;

            case USER_IS_BANNED:
                messageString = "You have been banned due to a large number of login attempts\n"
                        + "Please try again in " + customMessage[0] + " seconds";
                break;
            default:
                messageString = "Protocol Message case is not defined";
        }
        return messageString;
    }
}