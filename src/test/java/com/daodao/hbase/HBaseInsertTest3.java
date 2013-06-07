package com.daodao.hbase;

import com.daodao.hbase.TestKeyValueSkipListSet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-1-9
 *         Time: 下午5:54
 *         测试hbase mvcc jira改进性能.
 */
public class HBaseInsertTest3 {
    volatile TestKeyValueSkipListSet kvset;
    class HBaseInsertTask extends Thread{
        @Override
        public void run(){

        }
    }

    public  void test() {
        for(int i=0;i<100;i++){
           new HBaseInsertTask().start();
        }
    }

    public static void main(String[] args) {
         new HBaseInsertTest3().test();
        CountDownLatch rowLatch = new CountDownLatch(1);

        final ConcurrentHashMap<String, CountDownLatch> lockedRows =
                new ConcurrentHashMap<String, CountDownLatch>();

        CountDownLatch latch = lockedRows.putIfAbsent("1",rowLatch);

        System.out.println(latch==null);
        latch = lockedRows.putIfAbsent("2",new CountDownLatch(1));

        System.out.println(latch==null);

    }
}
