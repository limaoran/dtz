package com.rrkj.dtz.server;

import com.rrkj.util.RemoteClassLoaderContext;

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

            // 如果传输的是全局jar文件
            for(;obj instanceof JarInfo;){
                JarInfo jarInfo = (JarInfo) obj;
                RemoteClassLoaderContext.registerJar(jarInfo);
                obj = ois.readObject();
            }
            if(obj instanceof CommandInfo){
                CommandInfo commandInfo = (CommandInfo) obj;
                if(commandInfo.getJarInfo()!=null){
                    // 注册临时jar
                    RemoteClassLoaderContext.registerJar(commandInfo.getJarInfo());
                }
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

    /**
     * 默认的执行方法
     * @param commandInfo
     * @return
     * @throws Exception
     */
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
                // 方法1
                argsClasses[i] = getClass4Str(spt[i]);
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
        Object result =  method.invoke(obj,argsArray);
        return result;
    }
    private Class getClass4Str(String str) throws ClassNotFoundException {
        Object obj = completeClassStr(str);
        return obj instanceof Class?(Class)obj:Class.forName((String)obj);
    }
    /**
     * 根据短名称的class，补全
     * @param cls
     * @return 如果时基本类型则返回Class对象，否则返回类全名
     */
    public static Object completeClassStr(String cls){
        switch (cls){
            // 返回基本类型class
            case "byte":return byte.class;
            case "byte[]":return byte[].class;
            case "int": return int.class;
            case "int[]" : return int[].class;
            case "short":return short.class;
            case "short[]":return short[].class;
            case "long":return long.class;
            case "long[]":return long[].class;
            case "float":return float.class;
            case "float[]":return float[].class;
            case "double":return double.class;
            case "double[]":return double[].class;
            case "boolean" : return boolean.class;
            case "boolean[]" : return boolean[].class;
            case "char":
            case "character":return char.class;
            case "char[]":
            case "character[]":return char[].class;
        }
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
            case "character":return "java.lang.Character";
            case "byte":return "java.lang.Byte";
            case "list":return "java.util.List";
            case "map":return "java.util.Map";
            case "hashmap":return "java.util.HashMap";

        }
        return cls;
    }

}
