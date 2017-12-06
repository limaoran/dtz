package com.rrkj.dtz.test;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class MapTest {
    @Test
    public void testLinkedHashMap(){
        Map map = new LinkedHashMap();
        map.put("1",1);
        map.put("3",3);
        map.put("2",2);
        map.put("4",4);
        map.forEach((k,v)->{
            System.out.println(k+"->"+v);
        });
    }
}
