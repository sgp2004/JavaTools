package com.daodao.concurrent;

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
public class IntegerTest {
    class NormalInt {
        public int value=0;

        public synchronized void add() {
            this.value++;
        }

        public synchronized void sub() {
            this.value = this.value - 1;
        }
    }

    class ConcurrentInt {
        public AtomicInteger value=new AtomicInteger(0);

        public void add() {
            this.value.incrementAndGet();
        }

        public void sub() {
            this.value.decrementAndGet();
        }
    }

    class IntTask implements Runnable {
        NormalInt normalInt;

        @Override
        public void run() {
            normalInt = new NormalInt();
            long time = System.currentTimeMillis();
            while (normalInt.value<10000000)
                normalInt.add();
            System.out.println("finished in:" + (System.currentTimeMillis() - time));

        }
    }

    class ConcurrentIntTask implements Runnable {
         ConcurrentInt concurrentInt;
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            concurrentInt = new ConcurrentInt();
            while (concurrentInt.value.get()<10000000)   {
                concurrentInt.value.incrementAndGet();
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
        IntegerTest integerTest = new IntegerTest();
        integerTest.test();

    }

    private void test() {
        int threadNum = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        final long  time = System.currentTimeMillis();

        for(int i=0;i<threadNum;i++){
            executorService.execute(new ConcurrentIntTask());
            //executorService.execute(new ConcurrentIntTask());
        }
        executorService.shutdown();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run() {
                System.out.println("total time:"+(System.currentTimeMillis()-time));
            }
        });

    }
}
