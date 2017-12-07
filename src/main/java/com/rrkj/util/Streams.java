package com.rrkj.util;

import java.io.*;

import static com.rrkj.dtz.Constants.*;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class Streams {

    public static byte[] getBytes(InputStream is){
        try {
            byte[] bs = new byte[is.available()];
            is.read(bs,0,is.available());
            if(DEBUG) {
                System.out.println("======================InputStream中的内容===========start=========");
                System.out.println(new String(bs));
                System.out.println("====================================================end==========");
            }
            return bs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean copy(InputStream is, OutputStream os){
        byte[] bs = new byte[1024];
        try {
            for(int len =0; (len=is.read(bs))>=0 ;){
                os.write(bs,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {

        }
        return true;
    }
    public static void close(InputStream is){
        if(is!=null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(OutputStream os){
        if(os!=null){
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据每行获取流数据，之后会关闭流，请注意！
     */
    public static void getLines(InputStream is,String charset, CallbackLine cl) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is,charset));){
            for(String l;(l=br.readLine())!=null;){
                cl.line(l);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void write(byte[] bs, OutputStream os) throws IOException{
        os.write(bs);
    }
    public static void writeAndClose(byte[]bs,String file)throws IOException{
        try(OutputStream os = new FileOutputStream(file)){
            os.write(bs);
        }catch (Exception ex){
            throw ex;
        }
    }

    static interface CallbackLine{
        void line(String line);
    }
}
