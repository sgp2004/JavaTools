package com.daodao.utils;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-5-26
 *         Time: 下午11:59
 *         To change this template use File | Settings | File Templates.
 */
class Super{
    public Super(){
        System.out.println("super");
    }
}
class SuperWhat  implements Serializable {
    static final long serialVersionUID = 6328947014421475877L;

    public SuperWhat(){
        super();
    }
    public static void main(String[] args) {
        System.out.println("test");
        new SuperWhat();
    }

}
