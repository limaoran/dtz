package com.rrkj.dtz;

import java.util.Properties;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class Constants {
    public final static boolean DEBUG ;
    public final static boolean WARNING ;
    public final static boolean FINE ;

    public final static String JAR_DUMP_DIR;

    static {
        Properties props = new Properties();
        try{
            props.load(Constants.class.getResourceAsStream("/conf.properties"));
        }catch(Exception e){
            e.printStackTrace();
        }
        // 初始化常量
        DEBUG = "true".equals( props.getProperty("debug"));
        WARNING = "true".equals( props.getProperty("warning"));
        FINE = "true".equals( props.getProperty("fine"));

        JAR_DUMP_DIR = props.getProperty("jar_dump_dir");
    }
}
