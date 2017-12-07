package com.rrkj.dtz;

import com.rrkj.dtz.client.Client;
import com.rrkj.dtz.server.CommandInfo;

import java.util.Arrays;

/**
 * 启动Client
 * Created by Limaoran on 2017/12/7.
 */
public class ClientApp {
    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost",2181);
        client.start();
        client.addCommand("com.rrkj.dtz.demo.Hello@sayHello(String)","李明");
        client.addCommand("com.rrkj.dtz.demo.Hello@sayHello",null);
        client.addCommand("com.rrkj.dtz.demo.Hello@@hello",null);

        CommandInfo ci4 = new CommandInfo("com.rrkj.dtz.demo.Hello@@sum(Int,Int)",new Object[]{1,2});
        CommandInfo ci5 = new CommandInfo("com.rrkj.dtz.demo.Hello@sum2(Int,Int)",new Object[]{2,3});
        CommandInfo ci6 = new CommandInfo("com.rrkj.dtz.demo.Hello@@sum(List)", Arrays.asList(1,2,3,4,5));
        CommandInfo ci7 = new CommandInfo("com.rrkj.dtz.demo.Hello@createList(String,String,String)",new Object[]{"a","b","c"});

        CommandInfo ci8 = new CommandInfo("com.rrkj.dtz.demo.Hello@sum3(int,int)",new Object[]{4,5});

        client.addCommand(ci4);
        client.addCommand(ci5);
        client.addCommand(ci6);
        client.addCommand(ci7);
        client.addCommand(ci8);

        System.in.read();
        client.close();
    }
}
