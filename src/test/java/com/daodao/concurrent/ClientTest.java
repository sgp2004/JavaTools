package com.daodao.concurrent;

import org.apache.commons.codec.binary.Base64;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-3-4
 *         Time: 上午7:37
 *         To change this template use File | Settings | File Templates.
 */
public class ClientTest {
    public static void main(String[] args) {
        System.out.println( Long.parseLong("e0".substring(0,2),16));
        String test =  Base64.encodeBase64String(("test_a@sina.com:123")
                .getBytes());
        System.out.println(test.equals("dGVzdF9hQHNpbmEuY29tOjEyMw==\r\n"));
        System.out.println( Base64.encodeBase64String(("test_a@sina.com:123")
                .getBytes()));
    }

}
