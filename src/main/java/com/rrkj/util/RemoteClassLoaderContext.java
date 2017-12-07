package com.rrkj.util;

import com.rrkj.dtz.server.JarInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * server加载远程传输jar文件的classloader
 * Created by Limaoran on 2017/12/7.
 */
public class RemoteClassLoaderContext {

    private static URLClassLoader classLoader ;
    static {
        classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    }

    public static void loadJar(URL url) throws Exception {
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(classLoader, url);
    }
    public static void registerJar(JarInfo jarInfo)throws Exception{
        URL[] urls = new URL[jarInfo.getJarPath().length];
        for(int i=0;i<urls.length;i++){
            urls[i] = new File(jarInfo.getJarPath()[i]).toURI().toURL();
            loadJar(urls[i]);
        }
    }
}
