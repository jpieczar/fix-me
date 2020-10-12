package com.wtc;

import com.wtc.fixprotocol.Creator;
import com.wtc.fixprotocol.Validator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args){
		Creator c = new Creator(19);
		Validator v = new Validator();

		System.out.println(c.createIOI("N", "1", "S"));
		System.out.println(v.validateMessage(c.createIOI("N", "1", "S")));
    }
}
