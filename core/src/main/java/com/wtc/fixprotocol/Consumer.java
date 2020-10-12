package com.wtc.fixprotocol;

import java.util.*;

/**
 * Exposes some classes that make processing a fix
 * message simpler.
 * */
public class Consumer {
	private Dictionary<String, String> messageTokens;

	public Consumer(String message) {
		messageTokens = new Hashtable<String, String>();
		tokenizeMessage(message);

	}

	private void tokenizeMessage(String message){
		StringTokenizer stk = new StringTokenizer(message, "|");

		while (stk.hasMoreTokens()){
			String tmp = stk.nextToken();
			String[] a = tmp.split("=");
			messageTokens.put(a[0], a[1]);
		}
	}

	public String getTagValue(String point){
		String out = null;

		out = messageTokens.get(point);
		
		return out;
	}
}
