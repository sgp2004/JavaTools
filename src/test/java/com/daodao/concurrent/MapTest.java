package com.daodao.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-3-9
 *         Time: 上午8:03
 *       atomic跟普通int比总要快一倍
 *       但4核机器测试 到1000线程时 则 int要快 单线程循环1000w次， 13秒 vs 22秒
 */
public class MapTest {
    class NormalMap {
        public Map map=new HashMap();

        public synchronized void add(String k,String v) {
            this.map.put(k,v);
        }
    }

    class ConcurrentMap {
        public Map map=new ConcurrentSkipListMap();

        public void add(String k,String v) {
            this.map.put(k,v);
        }


    }

    class IntTask implements Runnable {
        NormalMap normalMap;

        @Override
        public void run() {
            normalMap = new NormalMap();
            long time = System.currentTimeMillis();
            int i = 0;
            while (i++<100000000)
               normalMap.add("test",i+"");
            System.out.println("finished in:" + (System.currentTimeMillis() - time));

        }
    }

    class ConcurrentIntTask implements Runnable {
        ConcurrentMap concurrentMap ;
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            concurrentMap = new ConcurrentMap();
            int i = 0;
            while (i++<100000000)   {
                concurrentMap.add("test",i+"");
//                try {
//                    TimeUnit.NANOSECONDS.sleep(1l);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
            }
            System.out.println("finished in:" + (System.currentTimeMillis() - time));

        }
    }

    public static void main(String[] args) {
        MapTest integerTest = new MapTest();
        integerTest.test();

    }

    private void test() {
        int threadNum = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        final long  time = System.currentTimeMillis();

        for(int i=0;i<threadNum;i++){
//            executorService.execute(new IntTask());
            executorService.execute(new ConcurrentIntTask());
        }
        executorService.shutdown();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run() {
                System.out.println("total time:"+(System.currentTimeMillis()-time));
            }
        });

    }
}
