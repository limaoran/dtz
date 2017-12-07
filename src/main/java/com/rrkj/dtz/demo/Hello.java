package com.rrkj.dtz.demo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Limaoran on 2017/12/6.
 */
public class Hello {
    public void sayHello(){
        System.out.println("hello");
    }
    public String sayHello(String name){
        System.out.println("hello "+name);
        return "hello "+name;
    }
    public static void hello(){
        System.out.println("你好啊！");
    }
    public static Integer sum(Integer a,Integer b){
        return a+b;
    }
    public Integer sum2(Integer a,Integer b){
        return a*a+b*b;
    }
    public int sum3(int a,int b){
        return a*a+b*b+a*b;
    }
    public static Integer sum(List<Integer> list){
        int sum=0;
        for (Integer i : list) {
            sum+=i;
        }
        return sum;
    }
    public List<String> createList(String a,String b,String c){
        return Arrays.asList(a,b,c);
    }
}
