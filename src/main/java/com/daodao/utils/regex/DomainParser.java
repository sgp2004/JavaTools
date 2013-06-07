package com.daodao.utils.regex;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-1-10
 *         Time: 下午4:46
 *         主要用于解析和返回url中的域名，返回结果全部小写，截至二级域名。此处为通俗称谓，和标准二级域名不同，
 *         即“tech.sina.com.cn”.  sina.com.cn为一级，
 *         .往前再取，tech 为二级
 */
public class DomainParser {
    //@link  http://zh.wikipedia.org/wiki/顶级域名列表
    private static String[] TOP_DOMAINS = {
            "aero", //用于航空工业;
            "arpa", //DNS反向查询；
            "asia", //用于亚洲地区；
            "biz",  //商业；
            "cat",  //加泰罗尼亚语区都可注册；
            "co",   //Company;
            "com",  //商业机构，任何人都可以注册；
            "coop", //用于商业合作团体；
            "edu",  //教育机构；
            "gov",  //政府部门；
            "int",  //国际组织；
            "info", //网络信息服务组织；
            "jobs", //职业介绍;
            "mil",  //美国军事部门；
            "mobi", //用于移动通信领域；
            "museum", //用于博物馆；
            "name",  //用于个人；
            "net",  //网络组织，例如互联网服务供应商和维修商，现在任何人都可以注册；
            "org", //非盈利组织，任何人都可以注册；
            "pro", //用于会计、律师和医生；
            "tel", //用于电信行业;
            "travel", //用于旅游组织；
            "xxx"   //成人内容。
    };


    /*
       @return [secondaryDomain, topDomain]
    */
    public static String[] parseDomains(String str) {
        String domain = parseHost(str);
        if (domain == null) {
            return null;
        }

        String[] domains = parseNormalDomains(domain);
        if (domains != null) {
            return domains;
        }
        return parseSurprisedDomains(domain);
    }


    /* if parseHost error ,may return request at once*/
    public static String parseHost(String str) {
        String domain;
        try {
            URL url = new URL(str);
            domain = url.getHost();
            if (domain == null) {
                return null;
            }
        } catch (MalformedURLException e) {
            System.out.println("error:"+str);
            return null;
        }
        return domain.toLowerCase();
    }


    /*符合规范，如 tech.sina.com.cn  返回 顶级域名和二级域名
    * sina.com.cn 则只返回顶级域名 sina.com.cn */
    private static String[] parseNormalDomains(String domain) {
        for (String topDomain : TOP_DOMAINS) {
            if (domain.contains(topDomain)) {
                int length = domain.length();
                String end = domain.substring(domain.indexOf("." + topDomain), length);
                String start = domain.substring(0, domain.indexOf(end));
                if (start.contains(".")) {
                    String[] tmpArray = start.split("\\.");
                    return new String[]{tmpArray[tmpArray.length - 2], tmpArray[tmpArray.length - 1] + end};
                }
                return new String[]{start + end};
            }
        }
        return null;
    }

    /*不符合规范，如 a.t.cn  返回 顶级域名和二级域名
    * t.cn 则只返回顶级域名 t.cn */
    private static String[] parseSurprisedDomains(String domain) {
        if (domain.contains(".")) {
            String[] tmpArray = domain.split("\\.");
            if (tmpArray.length >= 3) {

                return new String[]{tmpArray[tmpArray.length - 3], tmpArray[tmpArray.length - 2] + "." + tmpArray[tmpArray.length - 1]};
            }
        }
        return new String[]{domain};
    }

    public static void main(String[] args) {
        String[] urls = {null, "","abc", "http://WEIBO.com/", "http://sina.com.cn/", "http://test.google.com.hk/",
                "http://last.me?query=a", "http://w.g.last.me?query=a", "http://i.a.t.sina.com.cn/"};
        for (String item : urls) {
            String[] domains = parseDomains(item);
            if (domains != null) {
                if (domains.length == 2) {
                    System.out.println("secondary domain: " + domains[0] + " topDomain:" + domains[1]);
                } else {
                    System.out.println("topDomain:" + domains[0]);
                }
            }
        }
    }
}
