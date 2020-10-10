package com.wtc;

import com.wtc.fixprotocol.Fix;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args){
        Fix fix = new Fix();

        System.out.println(fix.createMessage());
    }
}
