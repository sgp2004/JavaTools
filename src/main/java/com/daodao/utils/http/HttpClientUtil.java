package com.daodao.utils.http;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author tangfulin
 *
 */
public class HttpClientUtil {

    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int MAX_RET_LEN = 1024 * 1024;
    private static final int MAX_RETRY_TIME = 300;

    private static HttpClient fastClient = null;
    private static HttpClient safeClient = null;
    private static HttpClient redirectClient = null;
    private static HttpClient slowClient = null;
    private static HttpClient proxyClient = null;

    private static MultiThreadedHttpConnectionManager connectionManager;

    private static HttpClient createHttpclient(int maxConPerHost, int maxTotal, int conTimeout, int soTimeout) {
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connectionManager.getParams();
        params.setMaxTotalConnections(maxTotal);// 这个值要小于tomcat线程池是800
        params.setDefaultMaxConnectionsPerHost(maxConPerHost);
        params.setConnectionTimeout(conTimeout);
        params.setSoTimeout(soTimeout);
        HttpClientParams clientParams = new HttpClientParams();
        // 忽略cookie 避免 Cookie rejected 警告
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        HttpClient ret = new HttpClient(clientParams, connectionManager);

        return ret;
    }

    private static HttpClient createProxyHttpclient(int maxConPerHost, int maxTotal, int conTimeout, int soTimeout) {
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connectionManager.getParams();
        params.setMaxTotalConnections(maxTotal);// 这个值要小于tomcat线程池是800
        params.setDefaultMaxConnectionsPerHost(maxConPerHost);
        params.setConnectionTimeout(conTimeout);
        params.setSoTimeout(soTimeout);

        HttpClientParams clientParams = new HttpClientParams();
        // 忽略cookie 避免 Cookie rejected 警告
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        HttpClient ret = new HttpClient(clientParams, connectionManager);

        return ret;
    }

    static {
        fastClient = createHttpclient(50, 500, 1000, 500);
        safeClient = createHttpclient(100, 200, 1000, 300);
        redirectClient = createHttpclient(50, 50, 1000, 300);
        proxyClient = createProxyHttpclient(50, 50, 1000, 1000);
        slowClient = createHttpclient(50, 600, 3000, 5000);
    }


    public static void stop() {

    }

    public static String postAndGetContent(final String url, final Map<String, String> postData) {

                return postAndGetContent(fastClient, url, postData);

    }

    private static String postAndGetContent(HttpClient client, final String url, final Map<String, String> postData) {
        final long start = System.currentTimeMillis();
        int readLen = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PostMethod post = new PostMethod(url);

        HttpMethodParams params = new HttpMethodParams();
        params.setContentCharset("utf-8");
        post.setParams(params);
        post.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0)");
        post.setRequestHeader("Referer", "http://weibo.com");
        String result = null;
        boolean isSocketReset = true;
        while(isSocketReset){
            try {
                isSocketReset = false;
                if (postData != null && !postData.isEmpty()) {
                    List<NameValuePair> list = new ArrayList<NameValuePair>(postData.size());

                    for (Map.Entry<String, String> entry : postData.entrySet()) {
                        if (entry.getKey() != null && !entry.getKey().isEmpty())
                            list.add(new NameValuePair(entry.getKey(), entry.getValue().toString()));
                        else
                            post.setRequestEntity(new StringRequestEntity(entry.getValue().toString(), "text/xml", "utf-8"));
                    }
                    if (!list.isEmpty())
                        post.setRequestBody(list.toArray(new NameValuePair[list.size()]));
                }

                client.executeMethod(post);
                InputStream in = post.getResponseBodyAsStream();

                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) > 0) {
                    out.write(b, 0, len);
                    readLen += len;
                    if (readLen > MAX_RET_LEN){
                        log.warn("too long return url=" + url);
                        break;
                    }
                }
                in.close();

                result = new String(out.toByteArray(), "utf-8");

            } catch (final SocketException e) {
                if(System.currentTimeMillis() - start < MAX_RETRY_TIME)   //if connection reset,retry in 300ms  -  guanpu added
                    isSocketReset = true;
                log.warn("IOE SocketException " + url, e);
            }catch (Exception e) {
                log.warn("IOE " + url, e);
            } finally {
                post.releaseConnection();
            }
        }
        long spendTime = System.currentTimeMillis() - start;
        if (spendTime > 500) {
            log.warn("slow post to url:" + url + " " + spendTime + "\n" + result);
        } else if (log.isDebugEnabled()) {
            log.debug("post to url:" + url + " " + spendTime + "\n" + result);
        }

        return result;
    }

    public static String getContent(String url) {

                return getContent(fastClient, url);

    }

    private static String getContent(HttpClient client, String url) {
        final long start = System.currentTimeMillis();
        int readLen = 0;
        HttpMethod get = new GetMethod(url);
        HttpMethodParams params = new HttpMethodParams();
        params.setContentCharset("utf-8");
        get.setParams(params);
        get.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0)");
        get.setRequestHeader("Referer", "http://weibo.com");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String result = null;
        boolean isSocketReset = true;
        while(isSocketReset){
            try {
                isSocketReset = false;
                client.executeMethod(get);
                InputStream in = get.getResponseBodyAsStream();
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) > 0) {
                    out.write(b, 0, len);
                    readLen += len;
                    if (readLen > MAX_RET_LEN){
                        log.warn("too long return url=" + url);
                        break;
                    }

                }
                in.close();
                result = new String(out.toByteArray(),"utf-8");

            } catch (final SocketException e) {
                if(System.currentTimeMillis() - start < MAX_RETRY_TIME)    //if connection reset,retry in 300ms  -  guanpu added
                    isSocketReset = true;
                log.warn("IOE SocketException " + url, e);
            }catch (final Exception e) {
                log.warn("IOE " + url, e);
            } finally {
                get.releaseConnection();
            }
        }
        long spendTime = System.currentTimeMillis() - start;
        if (spendTime > 500) {
            log.warn("slow get url:" + url + " " + spendTime + "\n" + result);
        } else if (log.isDebugEnabled()) {
            log.debug("get url:" + url + " " + spendTime + "\n" + result);
        }

        return result;

    }

    public static final String getContentWithParam(final String urlBase, final String paramString) {
        final String url = urlBase + encodeURL(paramString);
        String rst = getContent(url);
        return rst;
    }

    public static final String getContentWithParamWithoutUrlencode(final String urlBase, final String paramString) {
        final String url = urlBase + paramString;
        String rst = getContent(url);
        return rst;
    }

    /**
     * sync call http
     *
     * @param url
     * @return
     */
    public static String getProxyContent(final String url) {
        return getContent(proxyClient, url);
    }

    public static final String getProxyContentWithParam(final String urlBase, final String paramString) {
        final String url = urlBase + encodeURL(paramString);
        String rst = getProxyContent(url);
        return rst;
    }

    public static Map<String, String> getContent(final String[] urls) {
        throw new RuntimeException("not implemented yet!");
    }


    public static String getContentType(String url){
            return getContentType(redirectClient, url);

    }

    private static String getContentType(HttpClient client, String url) {
        final long start = System.currentTimeMillis();
        HeadMethod head = new HeadMethod(url);

        String result = null;
        try {

            client.executeMethod(head);
            Header[] headers = head.getResponseHeaders();
            for (Header header : headers) {
                if ("Content-Type".equals(header.getName())) {
                    result = header.getValue();
                }
            }

        } catch (final Exception e) {
            log.warn("IOE " + url, e);
        } finally {
            head.releaseConnection();
        }

        long spendTime = System.currentTimeMillis() - start;
        if (spendTime > 500) {
            log.warn("slow get content type url:" + url + " " + spendTime + "\n" + result);
        } else if (log.isDebugEnabled()) {
            log.debug("get content type url:" + url + " " + spendTime + "\n" + result);
        }

        return result;
    }

    public static String getRedirect(final String url) {
            return getRedirect(redirectClient,url);
    }

    private static String getRedirect(HttpClient client, final String url) {
        final long start = System.currentTimeMillis();
        HttpMethod get = new GetMethod(url);
        get.setFollowRedirects(false);
        get.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0)");
        get.setRequestHeader("Referer", "http://weibo.com");

        String result = null;
        boolean isSocketReset = true;
        while(isSocketReset){
            try {
                isSocketReset = false;
                int statusCode = client.executeMethod(get);
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    // 从头中取出转向的地址
                    Header locationHeader = get.getResponseHeader("Location");
                    if (locationHeader != null) {
                        result = locationHeader.getValue();
                    }
                }

            } catch (final SocketException e) {
                if(System.currentTimeMillis() - start < MAX_RETRY_TIME)    //if connection reset,retry in 300ms  -  guanpu added
                    isSocketReset = true;
                log.warn("IOE SocketException " + url, e);
            }catch (final Exception e) {
                log.warn("IOE " + url, e);

            } finally {
                get.releaseConnection();
            }
        }
        long spendTime = System.currentTimeMillis() - start;
        if (spendTime > 500) {
            log.warn("slow get redirect url:" + url + " " + spendTime + "\n" + result);
        } else if (log.isDebugEnabled()) {
            log.debug("get redirect url:" + url + " " + spendTime + "\n" + result);
        }

        return result;
    }

    public static Map<String, String> getRedirect(final String[] urls) {
        throw new RuntimeException("not implemented yet!");
    }

    public static String encodeURL(final String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (final Exception e) {
            log.warn("encodeURL error: " + url + " " + e.getMessage());
            return url;
        }
    }

    public static String decodeURL(final String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (final Exception e) {
            log.warn("decodeURL error: " + url + " " + e.getMessage());
            return url;
        }
    }

    public static String encodeString(final String string) {
        return encodeString(string, null);
    }

    public static String encodeString(final String string, String charset) {
        if (string == null || string.isEmpty()) {
            return "";
        }

        if (charset == null) {
            charset = "UTF-8";
        }
        byte[] bytes = null;
        try {
            bytes = string.getBytes(charset);
        } catch (final UnsupportedEncodingException e) {
            bytes = string.getBytes();
        }

        final int len = bytes.length;
        final byte[] encoded = new byte[bytes.length * 3];
        int n = 0;
        boolean noEncode = true;

        for (int i = 0; i < len; i++) {
            final byte b = bytes[i];

            if (b == ' ') {
                noEncode = false;
                encoded[n++] = (byte) '+';
            } else if (b >= 'a' && b <= 'z' || b >= 'A' && b <= 'Z' || b >= '0' && b <= '9') {
                encoded[n++] = b;
            } else {
                noEncode = false;
                encoded[n++] = (byte) '%';
                byte nibble = (byte) ((b & 0xf0) >> 4);
                if (nibble >= 10) {
                    encoded[n++] = (byte) ('A' + nibble - 10);
                } else {
                    encoded[n++] = (byte) ('0' + nibble);
                }
                nibble = (byte) (b & 0xf);
                if (nibble >= 10) {
                    encoded[n++] = (byte) ('A' + nibble - 10);
                } else {
                    encoded[n++] = (byte) ('0' + nibble);
                }
            }
        }

        if (noEncode) {
            return string;
        }

        try {
            return new String(encoded, 0, n, charset);
        } catch (final UnsupportedEncodingException e) {
            return new String(encoded, 0, n);
        }
    }



    public static void testRedirect() {
        System.out.println("sina: " + getRedirect("http://sinaurl.cn/hGAwdI"));
        //f*ck sohu! wired shorten link system!
        System.out.println("sohu: " + getRedirect("http://t.itc.cn/QNy0"));
        System.out.println("sohu: " + getRedirect(getRedirect("http://t.itc.cn/QNy0")));
        System.out.println("qq: " + getRedirect("http://url.cn/2ief7f"));
        System.out.println("163: " + getRedirect("http://163.fm/IYwTLi9"));
        System.out.println("ifen: " + getRedirect("http://url.ifeng.com/5NRC"));
        System.out.println("fl5: " + getRedirect("http://fl5.me/6mrj7m"));
        System.out.println("tiny: " + getRedirect("http://tinyurl.com/2b2kg9"));
        System.out.println("bitly: " + getRedirect("http://bit.ly/wyuc"));
        System.out.println("google: " + getRedirect("http://goo.gl/HXndr"));
        System.out.println("fb.me: " + getRedirect("http://fb.me/3Bkj7CW"));
    }


    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        //testRedirect();

        //test proxy
        String ret = getProxyContent("http://www.acfun.tv/api/gate.php?strict=yes&url=http://www.acfun.tv/v/ac263711/");
        System.out.println(ret);

        ret = postAndGetContent("http://www.acfun.tv/api/gate.php?strict=yes&url=http://www.acfun.tv/v/ac263711/",null);
        System.out.println(ret);

        System.out.println(getContentWithParamWithoutUrlencode("http://www.kuwo.cn/share/sina?link=","http://kzone.kuwo.cn/mlog/u43005304/kge_1200161.htm"));
    }

}
