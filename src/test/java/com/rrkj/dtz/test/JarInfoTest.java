package com.rrkj.dtz.test;

import com.rrkj.dtz.server.JarInfo;
import com.rrkj.util.RemoteClassLoaderContext;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Limaoran on 2017/12/7.
 */
public class JarInfoTest {
    JarInfo ji = new JarInfo(new String[]{
            "E:\\OneDrive\\lib\\mysql\\mysql-connector-java-5.1.10.jar",
            "E:\\OneDrive\\lib\\oracle\\classes12.jar",
//            "z:/commons-lang3-3.3.2.jar"
    });

    @Test
    public void testWrite()throws Exception{

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("z:/out.txt"));
        oos.writeObject(ji);
        oos.close();
    }
    @Test
    public void testRead()throws Exception{
        ObjectInputStream is = new ObjectInputStream(new FileInputStream("z:/out.txt"));
        JarInfo ji = (JarInfo) is.readObject();
        is.close();
        System.out.println(ji);
    }
    @Test
    public void testClassLoader()throws Exception{
        RemoteClassLoaderContext.registerJar(ji);
        System.out.println(Class.forName("java.lang.String"));
        System.out.println(Class.forName("com.mysql.jdbc.Driver"));
        System.out.println(Class.forName("oracle.jdbc.driver.OracleDriver"));
//        System.out.println(Class.forName("org.apache.commons.lang3.ArrayUtils"));
//        System.out.println(Class.forName("NumTest"));
    }
}
