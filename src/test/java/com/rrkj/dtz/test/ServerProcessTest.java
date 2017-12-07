package com.rrkj.dtz.test;

import com.rrkj.dtz.server.CommandInfo;
import com.rrkj.dtz.server.ServerProcesser;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Limaoran on 2017/12/7.
 */
public class ServerProcessTest {
    ServerProcesser sp = new ServerProcesser(null);
    @Test
    public void testParser()throws Exception{
        CommandInfo ci1 = new CommandInfo("com.rrkj.dtz.demo.Hello@sayHello(String)","李明");
        CommandInfo ci2 = new CommandInfo("com.rrkj.dtz.demo.Hello@sayHello",null);
        CommandInfo ci3 = new CommandInfo("com.rrkj.dtz.demo.Hello@@hello",null);

        CommandInfo ci4 = new CommandInfo("com.rrkj.dtz.demo.Hello@@sum(Int,Int)",new Object[]{1,2});
        CommandInfo ci5 = new CommandInfo("com.rrkj.dtz.demo.Hello@sum2(Int,Int)",new Object[]{2,3});
        CommandInfo ci6 = new CommandInfo("com.rrkj.dtz.demo.Hello@@sum(List)", Arrays.asList(1,2,3,4,5));
        CommandInfo ci7 = new CommandInfo("com.rrkj.dtz.demo.Hello@createList(String,String,String)",new Object[]{"a","b","c"});

        CommandInfo ci8 = new CommandInfo("com.rrkj.dtz.demo.Hello@sum3(int,int)",new Object[]{4,5});


        System.out.println("Result1:"+sp.executeMethod(ci1));;
         System.out.println("Result2:"+sp.executeMethod(ci2));
         System.out.println("Result3:"+sp.executeMethod(ci3));
         System.out.println("Result4:"+sp.executeMethod(ci4));
         System.out.println("Result5:"+sp.executeMethod(ci5));
         System.out.println("Result6:"+sp.executeMethod(ci6));
         System.out.println("Result7:"+sp.executeMethod(ci7));
        System.out.println("Result8:"+sp.executeMethod(ci8));;
    }
}
