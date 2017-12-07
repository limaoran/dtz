package com.rrkj.dtz;

import com.rrkj.dtz.client.Client;
import com.rrkj.dtz.server.CommandInfo;
import com.rrkj.dtz.server.JarInfo;

import java.util.Arrays;
import java.util.List;

/**
 * 启动Client
 * Created by Limaoran on 2017/12/7.
 */
public class ClientApp {
    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost",2181);
        client.start();

        CommandInfo ci4 = new CommandInfo("com.rrkj.dtz.demo.Hello@@sum(Int,Int)",new Object[]{1,2});
        CommandInfo ci5 = new CommandInfo("com.rrkj.dtz.demo.Hello@sum2(Int,Int)",new Object[]{2,3});
        CommandInfo ci6 = new CommandInfo("com.rrkj.dtz.demo.Hello@@sum(List)", Arrays.asList(1,2,3,4,5));
        CommandInfo ci7 = new CommandInfo("com.rrkj.dtz.demo.Hello@createList(String,String,String)",new Object[]{"a","b","c"});

        CommandInfo ci8 = new CommandInfo("com.rrkj.dtz.demo.Hello@sum3(int,int)",new Object[]{4,5});

        client.addCommand("com.rrkj.dtz.demo.Hello@sayHello(String)","李明");
        client.addCommand("com.rrkj.dtz.demo.Hello@sayHello",null);
        client.addCommand("com.rrkj.dtz.demo.Hello@@hello",null);
        client.addCommandInfo(ci4);
        client.addCommandInfo(ci5);
        client.addCommandInfo(ci6);
        client.addCommandInfo(ci7);
        client.addCommandInfo(ci8);

        JarInfo ji = new JarInfo(new String[]{
                "E:\\OneDrive\\lib\\mysql\\mysql-connector-java-5.1.10.jar",
                "E:\\OneDrive\\lib\\oracle\\classes12.jar"
        });

        CommandInfo ci9 = new CommandInfo("com.rrkj.dtz.demo.Hello@getJDBCClass",null,ji);
        client.addCommandInfo(ci9);

        // 设置全局的jar
        client.addGlobalJars(ji);

        CommandInfo ci10 = new CommandInfo("com.rrkj.dtz.demo.Hello@getOracleJDBC",null,null);
        client.addCommandInfo(ci10);

        // 自己的jar包
        JarInfo myJar = new JarInfo(new String[]{
                "z:/commons-lang3-3.3.2.jar"
        });

        List listArr = Arrays.asList(new Integer[]{1,2,3,4});
        CommandInfo ci11 = new CommandInfo("NumTest@sum(List)",listArr,myJar);
        client.addCommandInfo(ci11);

        int [] ars = new int[]{1,2,3,4,5,6};
        CommandInfo ci12 = new CommandInfo("NumTest@sum(int[])",ars,myJar);
        client.addCommandInfo(ci12);

        System.in.read();
        client.close();
    }
}
