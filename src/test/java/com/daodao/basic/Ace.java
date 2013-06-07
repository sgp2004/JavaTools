package com.daodao.basic;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-12-11
 *         Time: 上午10:33
 *         To change this template use File | Settings | File Templates.
 */
public class Ace {
    private   static   String name = "a";
    public void   setFileName(String filename){
       name = filename;
    }
    public  void test(){
        printtest();
    }
    private  void printtest(){
        System.out.println(name);
    }
}
