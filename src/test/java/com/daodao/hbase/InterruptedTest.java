package com.daodao.hbase;

import com.daodao.ObjectTestBase;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-12-9
 *         Time: 下午5:45
 *         To change this template use File | Settings | File Templates.
 */
public class InterruptedTest extends ObjectTestBase{
    private ICommonDao commonDao;

    @Before
    public void setUp() {
        commonDao = (ICommonDao) ctx.getBean("commonHbaseDao");
    }

    @Test
    public void testAdd() {
        commonDao.insert("test1","f1","key","value");

        String result = commonDao.getStrValue("test1", "f1", "key") ;
        System.out.println(result);
    }

    @Test
    public void testMultiAdd() throws InterruptedException {
        for (int i=0;i<100;i++){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("start thread"+finalI);
                    long  time = System.currentTimeMillis();
                    int loop=0;
                    while (loop++<1000)
//                        commonDao.insert("test1","f1","key","value"+ finalI);         //for one same key test1   1000 loop，100 threads cost 53s   ;if increase to 10000 loop cost 530s and may be timeout
                       commonDao.insert("test1"+finalI+loop,"f1","key","value"+ finalI); // for different key  1000 loop，100 threads cost 14s，10000loop cost 113s
                        //commonDao.delete("test1");         //for one same key test1   1000 loop，100 threads cost 53s
                       // commonDao.delete("test1"+finalI+loop); // for different key  1000 loop，100 threads cost 13s
                        //commonDao.incr("test3","f1","key",1l);  // for  one same key test2  1000 loop，100 threads cost 59s
                      // commonDao.incr("test3"+finalI+loop,"f1","key",1l);  // for different key  1000 loop，100 threads cost 15s
                   //  commonDao.getStrValue("test1","f1","key");  // for  one same key test1  100 loop，100 threads cost 59s ??? why is it so slow?
                       // commonDao.getStrValue("test1"+finalI+loop,"f1","key");  // for different key  1000 loop，100 threads cost 12s
                    System.out.println(finalI+"thread stop,use time:"+(System.currentTimeMillis()-time));
                }
            }) .start();
        }

        TimeUnit.DAYS.sleep(3l);
    }

//    @Test
//    public void testScan() {
//        Scan scan = new Scan();
//        scan.setStartRow();
//        scan.setStopRow();
//        ResultScanner resultScanner = commonDao.scan(scan);
//        for(Result result:resultScanner)
//        String result = commonDao.getStrValue("test1", "f1", "key") ;
//        System.out.println(result);
//    }
}
