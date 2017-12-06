package com.rrkj.dtz.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class ServerProcesser implements Runnable {
    private volatile boolean running ;
    ServerSocket server;

    public ServerProcesser(ServerSocket server) {
        running = true;
        this.server = server;
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        //
        Socket client = null;
        try {
            while(running){
                client = server.accept();
                System.out.println("有客户端建立连接！"+client.getInetAddress().getHostAddress()+":"+client.getPort());
                process(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void process(Socket client){
        try {
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            Object obj = ois.readObject();
            System.out.println("读取到客户端数据："+obj);
            if(obj instanceof CommandInfo){
                CommandInfo commandInfo = (CommandInfo) obj;
                Object result = executeMethod(commandInfo);
                System.out.println("客户端任务执行结果："+result);
//                if(result!=null) {
                    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                    oos.writeObject(result);
//                }
            }else{
                System.err.println("不支持的类型："+obj.getClass().getName());
                // 直接关闭
            }
        } catch (Exception e) {
            System.err.println("执行任务异常！"+e.getMessage());
            e.printStackTrace();
        }finally {
            if(client.isConnected()) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public Object executeMethod(CommandInfo commandInfo)throws Exception{
        // 解析命令 com.rrkj.demo.Hello@hello()
        boolean staticMethod = false; // 对象方法
        if(commandInfo.getCommand().contains("@@")) {
            staticMethod = true;    // 静态方法
        }
        Object args = commandInfo.getArgs();
        Object[]argsArray = null;

        String [] splited = commandInfo.getCommand().split(staticMethod?"@@":"@");
        String cls = splited[0];
        String methodStr = splited[1];
        Class[]argsClasses = null;
        if(methodStr.contains("(")){
            String[]spt = methodStr.split("[(]");
            methodStr = spt[0];
            String argsTemp = spt[1].substring(0,spt[1].length()-1);
            spt = argsTemp.split(",");
            argsClasses = new Class[spt.length];
            if(argsClasses.length>1){
                argsArray = (Object[]) args;
            }else{
                argsArray = new Object[1];
                argsArray[0] = args;
            }
            for(int i=0;i<spt.length;i++){
                // 方法1  目前bug：方法中存在基本类型int、long这种会有问题，先使用Integer,Long
                argsClasses[i] = Class.forName(completeClassStr(spt[i]));
                // 方法2
//                argsClasses[i] = argsArray[i].getClass();
            }
        }

        // 加载class
        Class clazz = Class.forName(cls);
        // 可以从spring中获取实例对象

        Method method = clazz.getMethod(methodStr,argsClasses);
        Object obj = null;
        if(!staticMethod){
            obj = clazz.newInstance();
        }
//        Object result =  method.invoke(obj,args);
        Object result =  method.invoke(obj,argsArray);
        return result;
    }

    /**
     * 根据短名称的class，补全
     * @param cls
     * @return
     */
    private String completeClassStr(String cls){
        switch (cls.toLowerCase()){
            case "string":return "java.lang.String";
            case "int":
            case "integer":return "java.lang.Integer";
            case "short":return "java.lang.Short";
            case "long":return "java.lang.Long";
            case "float":return "java.lang.Float";
            case "double":return "java.lang.Double";
            case "boolean" : return "java.lang.Boolean";
            case "char":
            case "charset":return "java.lang.Character";
            case "byte":return "java.lang.Byte";
            case "list":return "java.util.List";
        }
        return cls;
    }
}
