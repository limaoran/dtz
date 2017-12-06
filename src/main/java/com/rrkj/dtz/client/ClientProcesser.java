package com.rrkj.dtz.client;

import com.rrkj.dtz.server.CommandInfo;
import com.rrkj.dtz.server.ServerInfo;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 与服务端交互的Client
 * Created by Limaoran on 2017/12/6.
 */
public class ClientProcesser implements Runnable{
    private ServerInfo serverInfo;
    private CommandInfo commandInfo;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private CallbackResult callback;
    private  Socket client ;

    public ClientProcesser(ServerInfo serverInfo,CommandInfo commandInfo) {
        this.serverInfo = serverInfo;
        this.commandInfo = commandInfo;
        client = new Socket();
    }

    @Override
    public void run() {
        try{
            // 连接服务器
            client.connect(new InetSocketAddress(serverInfo.getIp(),serverInfo.getPort()));
            // 发送消息
            oos = new ObjectOutputStream(client.getOutputStream());
            sendCommand();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(client.isConnected()){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendCommand(){
        try {
            // 发动任务
            oos.writeObject(commandInfo);
            // 读取结果
            ois = new ObjectInputStream(client.getInputStream());
            Object result = ois.readObject();
            System.out.println("读取来自服务端的数据："+result);
            callback.callback(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public<T> void setResultCallback(CallbackResult<T> callback){
        this.callback = callback;
    }
}
