package com.rrkj.dtz.server;

import com.rrkj.dtz.Constants;
import com.rrkj.util.Streams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于传输jar包 分为全局和单次
 * Created by Limaoran on 2017/12/7.
 */
public class JarInfo implements Serializable,EntryInfo{
    /**
     * jar文件路径
     */
    private transient String[] jarPath;
    /**
     * jar文件内容
     */
    private transient List<byte[]> jarContent;

    public String[] getJarPath() {
        return jarPath;
    }

    public List<byte[]> getJarContent() {
        return jarContent;
    }

    public JarInfo(String []jarPath) {
        this.jarPath = jarPath;
        jarContent = new ArrayList<>(jarPath.length);
    }
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        // 先写入数组个数
        out.writeInt(jarPath.length);
        for(String path : jarPath){
            try(InputStream is = new FileInputStream(path)){
                // 写入jar文件长度
                byte[]bs = Streams.getBytes(is);
                System.out.println("file length:"+bs.length);
                jarContent.add(bs);
                out.writeInt(bs.length);
                out.writeObject(bs);  // 写入数据
            }catch(IOException e){
                throw e;
            }
        }
    }
    private void readObject(java.io.ObjectInputStream in)  throws IOException, ClassNotFoundException{
        int count = in.readInt();
        int dataLength = 0;
        jarPath = new String[count];
        for(int i=0;i<count;i++){
            // 读取数据长度
            dataLength = in.readInt();
            System.out.println("file length:"+dataLength);
            byte[] bs = (byte[]) in.readObject();
            // 临时存放
            jarPath[i] = Constants.JAR_DUMP_DIR+"/"+System.nanoTime()+"_"+i+".jar";
            Streams.writeAndClose(bs,jarPath[i]);
        }
    }
    private void readObjectNoData(){
        System.out.println("readObjectNoData");
    }
}
