package com.rrkj.util;

import com.rrkj.dtz.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class CommandUtils {
    /**
     * 使用CMD命令获取本地ip地址，ipv4
     * @return 多个ip用“,”分割
     */
    public static String getLocalIPAddress4CMD(){
        StringBuilder sb = new StringBuilder();
        String command = "cmd.exe /c ipconfig | findstr IPv4";
        try{
            Process p = Runtime.getRuntime().exec(command);
            Streams.getLines(p.getInputStream(),"GBK",line->{
                if(Constants.DEBUG){
                    System.out.println(line);
                }
                sb.append(line.split(":")[1].trim()+",");
            });
            if(sb.length()>0){
                sb.deleteCharAt(sb.length()-1);
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getLocalIPForJava(){
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress()
                            && inetAddress.isSiteLocalAddress()) {
                        sb.append(inetAddress.getHostAddress().toString()+"\n");
                    }
                }
            }
        } catch (SocketException e) {  }
        return sb.toString();
    }
    public static void main(String[] args) {
        getLocalIPAddress4CMD();
    }
}
