package com.daodao.utils;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-12-10
 *         Time: 下午2:57
 *         To change this template use File | Settings | File Templates.
 */
@FixMethodOrder(MethodSorters.JVM)
public class OrderTest {

    @Test public void addTest(){
        System.out.println("e");
    }
    @Test public void deleteTest(){
        System.out.println("b");
    }
    @Test public void moveTest(){
        System.out.println("c");
    }
}
