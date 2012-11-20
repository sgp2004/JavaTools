package com.daodao.hbase;

import com.daodao.ObjectTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-11-12
 *         Time: 下午4:39
 *         To change this template use File | Settings | File Templates.
 */
public class HbaseTest extends ObjectTestBase {
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
}
