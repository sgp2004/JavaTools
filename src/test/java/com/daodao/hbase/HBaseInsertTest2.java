package com.daodao.hbase;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-1-9
 *         Time: 下午5:54
 *         测试hbase 0.96 mvcc实现.
 */
public class HBaseInsertTest2 {
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
        new HBaseInsertTest2().test();
    }
}
