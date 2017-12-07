package com.rrkj.dtz.client;

import com.rrkj.dtz.server.CommandInfo;
import com.rrkj.dtz.server.ServerInfo;
import org.apache.zookeeper.*;

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

    private LinkedBlockingQueue<CommandInfo> queue = new LinkedBlockingQueue<>();

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
     * 添加命令
     * @param commandInfo
     */
    public void addCommand(CommandInfo commandInfo){
        queue.add(commandInfo);
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
                            CommandInfo ci = null;
                            try{
                                ci =  queue.take();
                                final CommandInfo commandInfo = ci;
                                byte[] bs =  zk.getData(ROOT_PATH+"/"+node,true, null);
                                System.out.println(new String(bs));
                                ServerInfo info = new ServerInfo(bs);
                                // 根据server信息，连接到server
                                ClientProcesser clientProcesser = new ClientProcesser(info,ci);
                                // 执行命令
                                // 这里先不采用多线程了，直接执行
                                clientProcesser.setResultCallback(result -> {
                                    System.out.println("任务："+commandInfo+"执行结果："+result);
                                });
                                clientProcesser.run();
                            }catch(Exception e){
                                e.printStackTrace();
                                // 如果出现异常，但是ci不为null，表示可能服务端有问题
                                // 把数据放回队列
                                if(ci!=null){
                                    queue.add(ci);
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
