package com.daodao.utils;

import cn.sina.api.commons.util.JsonWrapper;
import com.daodao.utils.http.HttpClientUtil;
import com.weibo.api.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-12-13
 *         Time: 上午9:22
 *         To change this template use File | Settings | File Templates.
 */
public class JsonTest {
    public static void main(String[] args) {
        //final String info = HttpClientUtil.getContentWithParam("http://10.209.134.40:8080/foo?id=", "http://10.209.134.40:8080/foo?id=testabc");

        // 4 errcode return false
        try {
            System.out.println("http://r3.sinaimg.cn/201301/120/120/aHR0cDovL3d3dy5wZW9wbGUuY29tLmNuL21lZGlhZmlsZS9waWMvMjAxMzAxMDgvODIvMTY2NTA3OTM5NzM3NTU3ODY1NC5qcGcraHR0cDovL3BpYy5wZW9wbGUuY29tLmNuL24vMjAxMy8wMTA4L2MxMDE2LTIwMTMzNTk5Lmh0bWw=.jpg".length());

            JSONObject jsonObject = new JSONObject("{\"object_id\":\"1032<:81\",\"object_domain_id\":\"1032\",\"safe_status\":1,\"show_status\":1,\"last_modified\":\"Wed Dec 26 18:14:22 CST 2012\",\"object\":{\"id\":\"1032:81\",\"object_type\":\"collection\",\"display_name\":\"台州启辰D50即将到店，可预订订金5000元\",\"image\":{\"url\":\"http://r3.sinaimg.cn/201212/300/300/aHR0cDovL2ltZzIuYml0YXV0b2ltZy5jb20vYXV0b2FsYnVtLy9maWxlcy8yMDEyMDcxMC84MTIvMTAyNDEwODEyMjM4MTBfMjI3MjExN181NTB4MzY2X19tMS5KUEcraHR0cDovL25ld3MuYml0YXV0by5jb20vZGFvZGlhbi8yMDEyMTIyNS8wOTA1OTY3NzA3Lmh0bWw=.jpg\",\"width\":\"50\",\"height\":\"50\"},\"url\":\"http://apps.e.weibo.com/mediapublic/atomlist?stuff_id=81\",\"object_types\":[\"article\"],\"items\":[{\"summary\":\"来源：易车网作者:黄华易车网台州讯】近日易车网编辑从台州启辰温岭恒联专营店获悉，启辰D50即将到店...\",\"url\":\"http://view.sina.com/url/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vZGFvZGlhbi8yMDEyMTIyNS8wOTA1OTY3NzA3Lmh0bWw=＆k=2ccd6＆f=va\"},{\"summary\":\"【易车网新车】先前很多媒体报道过福特Ka即将推出换代车型...\",\"url\":\"http://view.sina.com/url/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vYWJyb2FkLzIwMTIxMjI1LzA5MDU5Njc3MDMuaHRtbA==＆k=b45e3＆f=va\"},{\"summary\":\"福田汽车欧马可品牌2013年商务年会在京隆重召开。...\",\"url\":\"http://view.sina.com/url/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vem9uZ2hlLzIwMTIxMjI1LzA5MDU5Njc3MTEuaHRtbA==＆k=77df3＆f=va\"},{\"id\":\"1593@5466\",\"display_name\":\"易车网\",\"summary\":\"【易车网新车】近日据保时捷高层人员证实，...\",\"url\":\"http://view.sina.com/url/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vYWJyb2FkLzIwMTIxMjI1LzA5MDU5Njc3MTQuaHRtbA==＆k=aec44＆f=va\"}],\"create_at\":\"December25,2012,9:53am\",\"updated\":\"December25,2012,9:53am\"}}");
            //need check if defined error, for the return jsonObject is not the result
            //or not has id as json key
            if (jsonObject.has("errcode") || !jsonObject.has("id")) {
                System.out.println("error");
            }
            jsonObject.put("object_id","abc");
            System.out.println(jsonObject);

            //XXXX是广告的短链后面的字母，异步获取card信息时将想广告部门传递该参数
           String exts = "{\"object_id\":\"1032:25760\"," +
                   "\"object\":{" +
                   "\"mobile\":{   " +
                   "\"page_id\":\"100909XXXX\"," +
                        "\"url\":{" +
                                 "\"status\":\"0\"," +
                         "}," +
                         "\"card\":{" +
                                "\"status\":\"1\"," +
                                 "\"is_asyn\":\"1\"" +
                          "}" +
                   "}" +
                   "}" +
                   "}";
            JSONArray ret = new JSONArray();



                JSONObject ext = new JSONObject(exts);


                ret.put(ext);
            System.out.println(ret);
           System.out.println(MD5Utils.md5("http://f.pp.com/xiangpian200050"));
            System.out.println(Bytes.toBytes(("http://abcvxsfewfjweofofidofjodsjfodsfjosdfsdfsfdsfdf.com")).length );
            System.out.println(StringUtils.isNumeric("12342342342434343"));


            JsonWrapper js = new JsonWrapper("{\"a\":\"a\"}");
            System.out.println(js.get("b"));
            long t = 2000000000000000000l;
            System.out.println(StringUtils.isNumeric("2000000000000000000"));
            System.out.println("{\"domain_id\":\"" +"ab"+ "\",\"object_id\":\"" +"cd"+ "\"}");
        } catch (final JSONException ex) {
            LoggerFactory.getLogger(JsonTest.class.getName()).warn(ex.getMessage());
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
