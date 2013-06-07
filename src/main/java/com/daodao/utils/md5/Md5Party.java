package com.daodao.utils.md5;

//import com.weibo.api.util.MD5Utils;
import cn.sina.api.commons.util.ApacheHttpClient;
import cn.sina.api.commons.util.ApiHttpClient;
import cn.sina.api.commons.util.JsonBuilder;
import cn.sina.api.commons.util.JsonWrapper;
import com.weibo.api.ExcepFactor;
import com.weibo.api.WeiboApiException;
import com.weibo.api.util.MD5Utils;
import org.yecht.Bytestring;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-11-21
 *         Time: 上午10:21
 *         To change this template use File | Settings | File Templates.
 */
public class Md5Party {
    public static void main(String[] args) {
       // long t = MD5Utils.md5("Test").getBytes()+ Bytes.toBytes(-1 * 1000l);
        System.out.println(MD5Utils.md5("test"));
       // System.out.println(Integer.parseInt("123.4.5.6"));
       // System.out.println('♠');//♥♣♦

        JsonWrapper jsonWrapper = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", "http://abc.com/test1");
        map.put("object_id", "6:");
        ApiHttpClient apacheHttpClient = new ApacheHttpClient();
        String result = apacheHttpClient.post("http://10.210.226.124:8082/sinaurl/secure/bind_object.json", map);
        try {
            jsonWrapper = new JsonWrapper(result);
        } catch (Exception e) {
            throw new WeiboApiException(ExcepFactor.E_DEFAULT, " shorturl result error");
        }
        JsonBuilder jsonBuilder = new JsonBuilder();
        try {
            if (jsonWrapper.get("types").equals("39")) {
                jsonBuilder.append("result", true);
                jsonBuilder.append("short_url", jsonWrapper.get("shortUrl"));

            } else {
                jsonBuilder.append("result", false);
                jsonBuilder.append("object_id", "abc");
                jsonBuilder.append("url_type", jsonWrapper.get("types"));
            }
        } catch (Exception e) {
            System.out.println("error"+e.getMessage());
            jsonBuilder.append("result", false);
            jsonBuilder.append("info", "get shorturl info error");
        }


        jsonBuilder.append("object_id", "abc");
        jsonBuilder.flip();
        System.out.println(jsonBuilder.toString());

        Map<String,Long>  maps = new ConcurrentHashMap<String, java.lang.Long>()       ;
        System.out.println(maps.get("a"));
    }
}
