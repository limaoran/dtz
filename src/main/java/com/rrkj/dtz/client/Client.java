package com.rrkj.dtz.client;

import com.rrkj.dtz.server.CommandInfo;
import com.rrkj.dtz.server.EntryInfo;
import com.rrkj.dtz.server.JarInfo;
import com.rrkj.dtz.server.ServerInfo;
import com.rrkj.util.RemoteClassLoaderContext;
import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 客户端
 * Created by Limaoran on 2017/12/6.
 */
public class Client {

    protected String connectString;
    protected ZooKeeper zk;
    protected int sessionTimeout = 30000;
    public static final String ROOT_PATH = "/dtz";
    private Thread clientThread;
    protected volatile boolean running = true;

    private LinkedBlockingQueue<EntryInfo> queue = new LinkedBlockingQueue<>();
    private List<JarInfo> jars = new ArrayList<>();

    public Client(String zkHost, int zkPort) {
        this(zkHost + ":" + zkPort);
    }

    public Client(String connectString) {
        this.connectString = connectString;
        // 初始化zk信息
        init();
    }

    /**
     * 初始化zk信息
     */
    private void init() {
        try {
            zk = new ZooKeeper(connectString, sessionTimeout, null);
        } catch (Exception e) {
            System.err.println("初始化Zookeeper失败，应用退出！");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 添加命令
     * @param commandStr
     * @param args
     */
    public void addCommand(String commandStr,Object args){
        queue.add(new CommandInfo(commandStr, args));
    }

    /**
     * 添加全局的jar
     * @param jarInfo
     */
    public void addGlobalJars(JarInfo jarInfo){
        synchronized (jars) {
            if(jars.contains(jarInfo)){
                return;
            }
            jars.add(jarInfo);
        }
        try {
            // 本地也加载一份
            RemoteClassLoaderContext.registerJar(jarInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加 命令/jar 对象
     * @param entryInfo
     */
    public void addCommandInfo(EntryInfo entryInfo){
        queue.add(entryInfo);
    }
    public void start() {
        clientThread = new Thread(commandListener);
        clientThread.start();
    }
    private Runnable commandListener = new Runnable() {
        @Override
        public void run() {
            while(running){
                // 先设定为 一个服务执行一个任务
                try {
                    // 从zk中获取server节点
                    try {
                        List<String> listNode = zk.getChildren(ROOT_PATH,true);
                        if(listNode==null || listNode.size()<1){
                            System.err.println("没有可用的服务端！程序退出");
                            System.exit(-1);
                        }
                        listNode.forEach(node->{    // node格式： 127.0.0.1:8080
                            EntryInfo ei = null;
                            try{
                                ei =  queue.take();
                                final EntryInfo entryInfo = ei;
                                byte[] bs =  zk.getData(ROOT_PATH+"/"+node,true, null);
                                System.out.println(new String(bs));
                                ServerInfo info = new ServerInfo(bs);
                                // 根据server信息，连接到server
                                ClientProcesser clientProcesser = new ClientProcesser(info);
                                // 发送全局jar
                                if(jars.size()>0){
                                    clientProcesser.sendJars(jars);
                                }
                                // 执行命令
                                // 这里先不采用多线程了，直接执行
                                clientProcesser.setResultCallback(result -> {
                                    System.out.println("任务："+entryInfo+"执行结果："+result);
                                });
                                clientProcesser.sendCommand(ei);
                            }catch(Exception e){
                                e.printStackTrace();
                                // 如果出现异常，但是ci不为null，表示可能服务端有问题
                                // 把数据放回队列
                                if(ei!=null){
                                    queue.add(ei);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void close() {
        running = false;
        try {
            clientThread.interrupt();
//            clientThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
