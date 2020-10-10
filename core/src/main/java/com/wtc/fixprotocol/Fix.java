package com.wtc.fixprotocol;

import java.time.LocalTime;


/**
 * This is generates the fix string, and will be used to validate the 
 * the fix string.
 * */
public class Fix {
    private static String SOH = "|";
    private String header;
    private String trail;
    private int id;


    public Fix() {
        this.header = tags.BeginString + "=FIX.4.2" + SOH; 
        this.header += tags.BodyLength + "= %s " + SOH;
        this.header += tags.MsgType + "= %s " + SOH;
        this.header += tags.SenderCompID + "= %s " + SOH;
        this.header += tags.TargetCompID + "= %s " + SOH;
        this.header += tags.MsgSeqNum + "= %s " + SOH;
        this.header += tags.SendingTime + "= %s " + SOH;
        this.id = 0;
    }
    public Fix(int id){
        this();
        this.id = id;
    }

    public void setID(int ID){
        this.id = ID;
    }

    /**
     * Creates the message string in FIX formate.
     *
     * for now it simple just creates a header and a trailer,
     * later on it should take some information, like the
     * recieverId and the message type and the actual message.
     * */
    public String createMessage(){
        String sendingTime =  (LocalTime.now()).toString();
        String messageType = "A";
        String senderID = String.valueOf(id);
        String targetID = "0";
        String messageSeq = "";

        String bodyLength = "10";

        String message = String.format(createHeader(), 
                bodyLength, messageType, senderID, targetID,
                messageSeq, sendingTime);
        
        return message + createTrail();
    }

    private String createHeader(){
        return this.header;
    }

    private String createTrail(){
        this.trail = tags.CheckSum + "=%s" + SOH;

        return this.trail;
    }
    

}


enum tags {
    BeginString  ("8"),
    BodyLength  ("9"),
    MsgType  ("35"),
    SenderCompID  ("49"),
    TargetCompID  ("56"),
    MsgSeqNum  ("34"),
    SendingTime  ("52"),

    CheckSum ("10");

    private String action;

    public String getAction(){
        return this.action;
    }

    private tags(String action){
        this.action = action;
    }
}
