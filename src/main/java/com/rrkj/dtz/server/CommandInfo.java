package com.rrkj.dtz.server;

import java.io.Serializable;

/**
 * 执行命令的封装，@代表对象的方法，@@表示静态方法
 *  比如：public void hello(){}
 *      封装后为：   com.rrkj.demo.Hello@hello()
 *  比如：public void hello(String name),null
 *      封装后为：   com.rrkj.demo.Hello@hello(String),name
 *  比如：public void hello(String name,int age)
 *      封装后为：   com.rrkj.demo.Hello@hello(String,int),new Object[]{name,age}
 *  比如：public int hello(String name,List<String>list )
 *      封装后为：   com.rrkj.demo.Hello@hello(String,List),new Object[]{name,list}
 */
public class CommandInfo implements Serializable{
    private String command;
    private Object args;

    public CommandInfo() {
    }

    public CommandInfo(String command, Object args) {
        this.command = command;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public Object getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "CommandInfo{" +
                "command='" + command + '\'' +
                ", args=" + args +
                '}';
    }
}
