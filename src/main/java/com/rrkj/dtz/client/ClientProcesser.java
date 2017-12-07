package com.rrkj.dtz.client;

import com.rrkj.dtz.server.EntryInfo;
import com.rrkj.dtz.server.JarInfo;
import com.rrkj.dtz.server.ServerInfo;
import com.rrkj.util.RemoteClassLoaderContext;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

/**
 * 与服务端交互的Client
 * Created by Limaoran on 2017/12/6.
 */
public class ClientProcesser {
    private ServerInfo serverInfo;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private CallbackResult callback;
    private  Socket client ;

    public ClientProcesser(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        client = new Socket();
        // 连接服务器
        try {
            client.connect(new InetSocketAddress(serverInfo.getIp(),serverInfo.getPort()));
            // 发送消息流对象
            oos = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(EntryInfo entryInfo){
        try {
            // 发动任务
            oos.writeObject(entryInfo);
            // 如果是注册jar，则报错
            if(entryInfo instanceof JarInfo){
                // 本地也加载一份
                throw new IllegalArgumentException("jar文件不单独发送，或者全局发送，或者设为CommandInfo的一部分来发送！");
            }
            // 读取结果
            ois = new ObjectInputStream(client.getInputStream());
            Object result = ois.readObject();
            System.out.println("读取来自服务端的数据："+result);
            callback.callback(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void close(){
        if(client.isConnected()){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public<T> void setResultCallback(CallbackResult<T> callback){
        this.callback = callback;
    }

    public void sendJars(List<JarInfo> jars) {
        for(JarInfo jar:jars) {
            try {
                oos.writeObject(jar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
