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


	public boolean validateMessage(String message){
		int charCount = 0;
		String section = message.substring(0, message.indexOf(Tags.CheckSum.getAction() + "="));

		charCount += asciiSum(section);
		charCount %= 256;
		System.out.println(charCount);

		return true;
	}
	
	private int asciiSum(String str){
		int sum = 0;

		System.out.println(str);
		for (char c : str.toCharArray()){
			sum += (int) c;
		}
		return sum;
	}
}
