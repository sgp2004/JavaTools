package com.daodao.hbase;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.regionserver.MultiVersionConsistencyControl;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-1-9
 *         Time: 下午5:53
 *         分析0.94 insert操作性能
 */
public class HBaseInsertTest1 {
    volatile TestKeyValueSkipListSet kvset;
    final ReentrantReadWriteLock lock =
            new ReentrantReadWriteLock();
    final ReentrantReadWriteLock updatesLock =
            new ReentrantReadWriteLock();
    private final MultiVersionConsistencyControl mvcc =
            new MultiVersionConsistencyControl();
    private static AtomicInteger finishedCount;
    private static AtomicLong mvccTime = new AtomicLong(0l);
    private static AtomicLong rowlockTime = new AtomicLong(0l);
    private static AtomicLong lockTime = new AtomicLong(0l);
    private static AtomicLong updateLockTime = new AtomicLong(0l);
    private static AtomicLong insertTime = new AtomicLong(0l);
    private static AtomicLong releaseTime = new AtomicLong(0l);

    private final ConcurrentHashMap<String, CountDownLatch> lockedRows =
            new ConcurrentHashMap<String, CountDownLatch>();

    public HBaseInsertTest1() {
        kvset = new TestKeyValueSkipListSet(new KeyValue.KVComparator());
        finishedCount = new AtomicInteger(0);
    }

    class HBaseInsertTask implements Runnable {

        public void run() {
            for (int i = 0; i < 100000; i++) {
                String key = "key";// + i;
                long time = System.nanoTime();
                MultiVersionConsistencyControl.WriteEntry localizedWriteEntry = null;
                try {


                    lock.readLock().lock();   // like startRegionOperation do
                    lockTime.set(lockTime.get() + (System.nanoTime() - time));

                    time = System.nanoTime();
                    Integer lid = getLock(key);     //get rowKey lock
                    lockTime.set(System.nanoTime() - time);

                    time = System.nanoTime();
                    updatesLock.readLock().lock();
                    updateLockTime.set(updateLockTime.get() + (System.nanoTime() - time));


//                    time = System.nanoTime();
//                    // wait for all prior MVCC transactions to finish - while we hold the row lock
//                    // (so that we are guaranteed to see the latest state)
//                    mvcc.completeMemstoreInsert(mvcc.beginMemstoreInsert());
//                    mvccTime.set(mvccTime.get() + (System.nanoTime() - time));

                    time = System.nanoTime();
                    localizedWriteEntry = mvcc.beginMemstoreInsert();
                   mvccTime.set(mvccTime.get() + (System.nanoTime() - time));

                    time = System.nanoTime();
                    kvset.add(new KeyValue(Bytes.toBytes(key), Bytes.toBytes("f"), Bytes.toBytes("column"),
                            1l, Bytes.toBytes(1l)));
                    insertTime.set(insertTime.get() + (System.nanoTime() - time));

                    time = System.nanoTime();
                    mvcc.completeMemstoreInsert(localizedWriteEntry);
                    mvccTime.set(mvccTime.get() + (System.nanoTime() - time));
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    time = System.nanoTime();
                    updatesLock.readLock().unlock();

                    CountDownLatch rowLatch = lockedRows.remove(key);
                    rowLatch.countDown();

                    lock.readLock().unlock();
                    releaseTime.set(releaseTime.get() + (System.nanoTime() - time));

                }
            }
            finishedCount.set(finishedCount.get() + 1);
            return;
        }

        private Integer getLock(String key) {
            CountDownLatch rowLatch = new CountDownLatch(1);

            // loop until we acquire the row lock (unless !waitForLock)
            while (true) {

                CountDownLatch existingLatch = lockedRows.putIfAbsent(key, rowLatch);
                if (existingLatch == null) {
                    break;
                } else {
                    try {
                        if (!existingLatch.await(30000,
                                TimeUnit.MILLISECONDS)) {
                            System.out.println("some thing wrong in waiting");
                            return null;
                        }
                    } catch (InterruptedException ie) {
                        // Empty
                    }
                }
            }
            return 1;
        }
    }

    private class DaodaoTestWatcher implements Runnable {

        @Override
        public void run() {
            long time = System.nanoTime();
            while (finishedCount.get() != 50) {

            }
            System.out.println("cost time:" + (System.nanoTime() - time) / 1000000000.0);
            System.out.println("cost time:  mvcc" + mvccTime.get() / 1000000000.0 );
            System.out.println("cost time:  lock" + lockTime.get() / 1000000000.0 );
            System.out.println("cost time:  insert" + insertTime.get() / 1000000000.0 );
            System.out.println("cost time:  update" + updateLockTime.get() / 1000000000.0);
            System.out.println("cost time:  rowlock" + rowlockTime.get() / 1000000000.0 );
            System.out.println("cost time:  release" + releaseTime.get() / 1000000000.0);
        }
    }

    public void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        //for(int j=0;j<10;j++){
        for (int i = 0; i < 50; i++)
            executorService.execute(new HBaseInsertTask());
//         finishedCount.set(0);
//         kvset.clear();
//         lockedRows.clear();
//     }
        executorService.execute(new DaodaoTestWatcher());


    }

    public static void main(String[] args) {
        new HBaseInsertTest1().test();
    }


}
