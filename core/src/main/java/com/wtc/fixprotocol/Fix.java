package com.wtc.fixprotocol;

import java.time.LocalTime;
import java.lang.StringBuilder;



/**
 * This is generates the fix string, and will be used to validate the 
 * the fix string.
 * */
public class Fix {
    private static String SOH = "|";
    private StringBuilder message;      // Do I even need this variable?
    private String trail;       // This one as well... they are function calls.
    private int id;


    public Fix() {
        this.message = new StringBuilder();
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


        createHeader(messageType, senderID, targetID, sendingTime);
        createTrail();

        return this.message.toString();

    }

    /**
     * This creates the header.
     * 
     * For now it is ignorant of the message content.. I'll do that later on.
     * */
    private void createHeader(String msgType, String sID, String tID, String sendingTime){
        this.message.append(tags.BeginString.getAction() + "=FIX.4.2" + SOH);
        this.message.append(tags.BodyLength.getAction() + "=BL" + SOH);
        this.message.append(tags.MsgType.getAction() + "=" + msgType + SOH);
        this.message.append(tags.SenderCompID.getAction() + "=" + sID + SOH);
        this.message.append(tags.TargetCompID.getAction() + "=" + tID + SOH);
        this.message.append(tags.SendingTime.getAction() + "=" + sendingTime + SOH);
        
        // Calculating the body length.
        int startPoint = message.indexOf("|", message.indexOf(tags.MsgType.getAction()));
        int BL = message.substring(startPoint).length();
        int bl_index = message.indexOf("BL");
        message.replace(bl_index, bl_index+2, String.valueOf(BL));
    }

    private void createTrail(){
        this.message.append(tags.CheckSum.getAction() + "=CHKSUM" + SOH);
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
