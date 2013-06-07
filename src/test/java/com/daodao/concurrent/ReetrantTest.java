package com.daodao.concurrent;

import org.apache.http.annotation.GuardedBy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-1-3
 *         Time: 上午7:45
 *         To change this template use File | Settings | File Templates.
 */
 class Widget {
    public synchronized void doSomething() {
        System.out.println("super doing");
        }
}
 class LoggingWidget extends Widget {
     private String[] test = {"ab","ac"};
     ThreadLocal<String[]> threadLocal = new ThreadLocal<String[]>(){
         public String[] initialValue(){return new String[]{"test"};}
     };

     public String[] getTest(){
         return threadLocal.get();
     }
     @GuardedBy("this")
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();
    }
}
public class ReetrantTest {
    public static void main(String[] args) {
        LoggingWidget loggingWidget = new LoggingWidget();
        loggingWidget.doSomething();

        System.out.println(loggingWidget.getTest()[0]);
        Map<String,String> t =  Collections.synchronizedMap(new HashMap<String, String>());
        t.put("t","1");
        t.put("t","2");
        System.out.println(t);

    }

}
