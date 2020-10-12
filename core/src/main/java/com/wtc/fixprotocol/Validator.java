package com.wtc.fixprotocol;

import java.util.*;
import com.wtc.utils.*;


/**
 * Validates fix messages.
 *
 * For now it just checks the checksum.
 * Maybe later it will also check for the message types so that
 * we can make sure the information is correct at all levels.
 * */
public class Validator {

	public Validator() {


	}

	/**
	 * Checks the check sum of the given message.
	 *
	 * For now it is just working with the checksum..
	 * Later on it should work wit h other parts of the 
	 * message to help with the validation.
	 * */
	public boolean validateMessage(String message){
		int charCount = 0;
		Consumer con = new Consumer(message);
		String section = message.substring(0, message.indexOf(Tags.CheckSum.getAction() + "="));

		// Calculate the ascii values
		charCount += asciiSum(section);
		charCount %= 256;

		int val = Integer.parseInt(con.getTagValue(Tags.CheckSum.getAction()));
		if (charCount == val)
			return true;
		return false;
	}
	
	private int asciiSum(String str){
		int sum = 0;

		for (char c : str.toCharArray()){
			sum += (int) c;
		}
		return sum;
	}
}
