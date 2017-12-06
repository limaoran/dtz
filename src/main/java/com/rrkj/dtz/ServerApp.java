package com.rrkj.dtz;

import com.rrkj.dtz.server.Server;

/**
 * 启动Server
 * Created by Limaoran on 2017/12/7.
 */
public class ServerApp {
    public static void main(String[] args)throws Exception {
        Server server = new Server("127.0.0.1:2181");
        server.start();
        System.in.read();
        server.close();
    }
}
