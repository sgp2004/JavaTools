package com.daodao.basic;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-12-11
 *         Time: 上午10:26
 *         To change this template use File | Settings | File Templates.
 */

public class ExtendTest {
    public static void main(String[] args) {
        Baby baby = new Baby();

        baby.setFileName("b");
        baby.test();
        new Ace().test();
        System.out.println("t".replace("t",null));
    }

}
