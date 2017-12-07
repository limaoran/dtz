package com.rrkj.dtz;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class Constants {
    public final static boolean DEBUG ;
    public final static boolean WARNING ;
    public final static boolean FINE ;

    public final static String JAR_DUMP_DIR;

    static {
        // 初始化常量
        DEBUG = false;
        WARNING = true;
        FINE = true;

        JAR_DUMP_DIR = "z:/udump";
    }
}
