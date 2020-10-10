package com.wtc;

import com.wtc.fixprotocol.Creator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args){
		Creator c = new Creator(19);

		System.out.println(c.createIOI("N", "1", "S"));
    }
}
