package com.rrkj.dtz.server;

import com.rrkj.util.CommandUtils;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static com.rrkj.dtz.Constants.*;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class Server {
    protected String connectionString ;
    protected int sessionTimeout = 3000;
    public static final String ROOT_PATH = "/dtz";
    protected String childPath ;
    protected ZooKeeper zk;
    protected ServerInfo info;
    protected Thread socketThread ;
    protected ServerProcesser serverProcess;

    public Server(String connectionString) {
        this.connectionString = connectionString;
        init();
    }

    /**
     * 初始化本地ip信息和绑定端口
     */
    private void init(){
        // 获取本地ip地址
        String ip = CommandUtils.getLocalIPAddress4CMD();
        int port = 9590;
        ServerSocket server = null;
        try {
            server =new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 尝试100次端口号
        for(int i=port;i<port+100;i++){
            try {
                if(DEBUG){
                    System.out.println("尝试使用端口号["+port+"]建立服务！");
                }
                server.bind(new InetSocketAddress(port));

                System.out.println("使用端口号["+port+"]建立服务成功！");
                break;
            } catch (BindException e) {
                // 端口占用，换下一个端口
                port++;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        serverProcess = new ServerProcesser(server);
        socketThread = new Thread(serverProcess);
        socketThread.start();
        info= new ServerInfo(ip,port);
        childPath = ROOT_PATH +"/"+ip+":"+port;
        System.out.println(info);
    }

    /**
     * 启动zk，启动server
     */
    public void start(){
        try {
            zk = new ZooKeeper(connectionString, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Watcher.Event.EventType.None) {
                        System.out.println("连接服务器成功！");
                    } else if (event.getType() == Watcher.Event.EventType.NodeCreated) {
                        System.out.println("节点创建成功！");
                    } else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                        System.out.println("子节点创建成功！");
                        //读取新的配置
                    } else if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                        System.out.println("节点更新成功！");
                    } else if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                        System.out.println("节点删除成功！");
                        //读取新的配置
                    }
                }
            });
            if(zk.exists(ROOT_PATH,false)==null) {
                // 创建一个目录节点
                zk.create(ROOT_PATH, "dtz server".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            // 创建一个子目录节点
            // 目录构成：ROOT_PATH/ip
            if(DEBUG){
                System.out.println("创建子节点目录："+childPath);
            }
            /*
            节点类型说明：
            节点类型有4种：“PERSISTENT、PERSISTENT_SEQUENTIAL、EPHEMERAL、EPHEMERAL_SEQUENTIAL”
            其中“EPHEMERAL、EPHEMERAL_SEQUENTIAL”两种是客户端断开连接(Session无效时)节点会被自动删除；
            “PERSISTENT_SEQUENTIAL、EPHEMERAL_SEQUENTIAL”两种是节点名后缀是一个自动增长序号。
            * */
            zk.create(childPath,info.toBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public void close(){
        try {
            serverProcess.stop();
            // 等待存在的任务执行完成
            socketThread.join();
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
