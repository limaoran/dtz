package com.rrkj.dtz.test;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class ZKTest {
    private ZooKeeper zk ;
    @Before
    public void before()throws Exception{
        zk = new ZooKeeper("localhost:2181",500,null);
    }
    @Test
    public void deleteChildNodes()throws Exception{
        List<String> list= zk.getChildren("/dtz",true);
        list.forEach(node->{
            try {
                zk.delete("/dtz/"+node,-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
