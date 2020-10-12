package com.wtc;

import com.wtc.fixprotocol.Creator;
import com.wtc.fixprotocol.Validator;
import com.wtc.fixprotocol.Consumer;

import com.wtc.utils.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args){
		Creator c = new Creator(19);
		Validator v = new Validator();
		Consumer con;

		System.out.println(c.createIOI("N", "1", "S"));
		System.out.println(v.validateMessage(c.createIOI("N", "1", "S")));
		con = new Consumer(c.createIOI("N", "1", "S"));

		System.out.println(con.getTagValue(Tags.BeginString.getAction()));
    }
}
