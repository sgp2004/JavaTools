package com.daodao;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-9-19
 *         Time: 下午3:19
 *         To change this template use File | Settings | File Templates.
 */
public class ObjectTestBase {
    private static final Logger log = Logger.getLogger(ObjectTestBase.class);

    public static ApplicationContext ctx = null;

    static {
        if (ctx != null) {
            log.error("Error: try to init ActivityTestUtil twice");
            throw new IllegalArgumentException("Error: try to init ActivityTestUtil twice");
        }
        try {
            ctx = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/hbase/toolsbox-hbase.xml"
            });


        } catch (Exception e) {
            log.error("init ActivityTestUtil error", e);
        }
    }


}
