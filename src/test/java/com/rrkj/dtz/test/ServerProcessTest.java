package com.rrkj.dtz.test;

import com.rrkj.dtz.server.CommandInfo;
import com.rrkj.dtz.server.JarInfo;
import com.rrkj.dtz.server.ServerProcesser;
import com.rrkj.util.RemoteClassLoaderContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
    @Test
    public void testClassLoader()throws Exception{
        JarInfo ji = new JarInfo(new String[]{
                "E:\\OneDrive\\lib\\mysql\\mysql-connector-java-5.1.10.jar",
                "E:\\OneDrive\\lib\\oracle\\classes12.jar"
        });
        RemoteClassLoaderContext.registerJar(ji);
        CommandInfo ci = new CommandInfo("com.rrkj.dtz.demo.Hello@getJDBCClass",null);
        System.out.println(this.getClass().getClassLoader());
        System.out.println(sp.executeMethod(ci));
        System.out.println(Class.forName("com.mysql.jdbc.Driver"));
    }
    @Test
    public void testClassLoaderMyJar()throws Exception{
        JarInfo myJar = new JarInfo(new String[]{
                "z:/commons-lang3-3.3.2.jar"
        });
        RemoteClassLoaderContext.registerJar(myJar);


        List listArr = Arrays.asList(new Integer[]{1,2,3,4});
        CommandInfo ci11 = new CommandInfo("NumTest@sum(List)",listArr,myJar);

        int [] ars = new int[]{1,2,3,4};
        CommandInfo ci12 = new CommandInfo("NumTest@sum(int[])",ars,myJar);

        System.out.println(sp.executeMethod(ci11));
        System.out.println(sp.executeMethod(ci12));
    }
}
