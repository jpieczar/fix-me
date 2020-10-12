package com.wtc.fixprotocol;

import java.time.LocalTime;
import java.lang.StringBuilder;

import com.wtc.utils.*;


/**
 * Formates message into valid fix messages
 * This is generates the fix string, and will be used to validate the
 * the fix string.
 * */
public class Creator {
    private static String SOH = "|";
    private StringBuilder message;      // Do I even need this variable?
    private String trail;       // This one as well... they are function calls.
    private int id;


    public Creator() {
        this.message = new StringBuilder();
        this.id = 0;
    }
    public Creator(int id){
        this();
        this.id = id;
    }

    public void setID(int ID){
        this.id = ID;
    }

	public String createIOI(String transType, String side, String shares){
		StringBuilder tmp = new StringBuilder();
		String msgType = "6";

		tmp.append(Tags.IOIId.getAction() + "=a" + String.valueOf(this.id)  + SOH);
		tmp.append(Tags.IOITransType.getAction() + "=" + transType + SOH);
		tmp.append(Tags.Side.getAction() + "=" + side + SOH);
		tmp.append(Tags.OrderQTY.getAction() + "=" + "10" + SOH );
		tmp.append(Tags.Price.getAction() + "=" + "100" + SOH);
		tmp.append(Tags.IOIShaces.getAction() + "=" + shares + SOH);

		return createMessage("6", tmp.toString());
	}

    /**
     * Creates the message string in FIX formate.
     *
     * for now it simple just creates a header and a trailer,
     * later on it should take some information, like the
     * recieverId and the message type and the actual message.
     * */
    private String createMessage(String msgType, String body){
        String sendingTime =  (LocalTime.now()).toString();
        String senderID = String.valueOf(id);
        String targetID = "0";

        createHeader(msgType, senderID, targetID, sendingTime);
		message.append(body);
        createTrail();

        return this.message.toString();
    }

    /**
     * This creates the header.
     *
     * For now it is ignorant of the message content.. I'll do that later on.
     * */
    private void createHeader(String msgType, String sID, String tID, String sendingTime){
        this.message.append(Tags.BeginString.getAction() + "=FIX.4.2" + SOH);
        this.message.append(Tags.BodyLength.getAction() + "=BL" + SOH);
        this.message.append(Tags.MsgType.getAction() + "=" + msgType + SOH);
        this.message.append(Tags.SenderCompID.getAction() + "=" + sID + SOH);
        this.message.append(Tags.TargetCompID.getAction() + "=" + tID + SOH);
        this.message.append(Tags.SendingTime.getAction() + "=" + sendingTime + SOH);
        
        // Calculating the body length.
        int startPoint = message.indexOf("|", message.indexOf(Tags.MsgType.getAction()));
        int BL = message.substring(startPoint).length();
        int bl_index = message.indexOf("BL");
        message.replace(bl_index, bl_index+2, String.valueOf(BL));
    }

    private void createTrail(){
		int sum = 0;

		for (char c : (message.toString()).toCharArray()){
			sum += (int) c;
		}
		System.out.println(sum);
		sum %= 256;
        this.message.append(Tags.CheckSum.getAction() + "=" + String.valueOf(sum) + SOH);
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
