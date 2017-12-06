package com.rrkj.dtz.server;

import java.io.Serializable;

/**
 * 存储服务器相关信息
 * Created by Limaoran on 2017/12/6.
 */
public class ServerInfo implements Serializable{
    static final long serialVersionUID = 1L;
    /**
     * 服务器对外提供的IP地址
     */
    private String ip;
    /**
     * 服务器对外提供的端口号
     */
    private int port;
    //TODO 由于功能简单，先用这两个，执行的任务数量控制还没有加入

    public byte[] toBytes(){
        String msg = ip + "-"+port;
        return msg.getBytes();
    }
    public ServerInfo(byte[] bs){
        String msg = new String(bs);
        String[] split = msg.split("-");
        ip = split[0];
        port = Integer.parseInt(split[1]);
    }

    public ServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

}
