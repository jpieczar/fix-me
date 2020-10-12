package com.wtc.utils;

public enum Tags {
	// Header tags
    BeginString  ("8"),
    BodyLength  ("9"),
    MsgType  ("35"),
    SenderCompID  ("49"),
    TargetCompID  ("56"),
    MsgSeqNum  ("34"),
    SendingTime  ("52"),
	// Trailer tags.
    CheckSum ("10"),
	// IOI tags
	IOIId ("23"),
	IOITransType ("28"),
	Side ("54"),
	IOIShaces ("27"),
	OrderQTY ("38"),
	Price ("44");

    private String action;

    public String getAction(){
        return this.action;
    }

    private Tags(String action){
        this.action = action;
    }

}
