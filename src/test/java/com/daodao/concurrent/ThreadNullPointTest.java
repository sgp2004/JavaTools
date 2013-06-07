package com.daodao.concurrent;

import cn.sina.api.commons.util.ApiLogger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-12-11
 *         Time: 下午5:05
 *         To change this template use File | Settings | File Templates.
 */
public class ThreadNullPointTest {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String none = null;
                String tname = Thread.currentThread().getName();
                Thread.currentThread().setName(tname + ":RuleManagerService");
                while (true) {
                    System.out.println("test");
                    try{
                    System.out.println(none.contains("abc"));
                    }catch (Exception e){

                    }
                }

            }
        }

        ).start();
    }
}
