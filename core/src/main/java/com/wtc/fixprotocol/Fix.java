package com.wtc.fixprotocol;



/**
 * This is generates the fix string, and will be used to validate the 
 * the fix string.
 * */
public class Fix {
    private static String SOH = "|";
    private String header;


    public Fix() {
        this.header = tags.BeginString + "= %s" + SOH; 
        this.header += tags.BodyLength + "= %s " + SOH;
        this.header += tags.MsgType + "= %s " + SOH;
        this.header += tags.SenderCompID + "= %s " + SOH;
        this.header += tags.TargetCompID + "= %s " + SOH;
        this.header += tags.MsgSeqNum + "= %s " + SOH;
        this.header += tags.SendingTime + "= %s " + SOH;

    }

    public String createHeader(){
        return this.header;
    }
    

}


enum tags {
    BeginString  ("8"),
    BodyLength  ("9"),
    MsgType  ("35"),
    SenderCompID  ("49"),
    TargetCompID  ("56"),
    MsgSeqNum  ("34"),
    SendingTime  ("52");

    private String action;

    public String getAction(){
        return this.action;
    }

    private tags(String action){
        this.action = action;
    }
}
