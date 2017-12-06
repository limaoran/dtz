package com.rrkj.dtz.test;

import com.rrkj.dtz.server.ServerInfo;
import org.junit.Test;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class ServerInfoTest {
    @Test
    public void testServerInfo() {
        ServerInfo info = new ServerInfo("127.0.0.1",8080);
        byte []bs = info.toBytes();
        System.out.println(new String(bs));
        System.out.println(new ServerInfo(bs));
    }
}
