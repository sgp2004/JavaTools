package com.daodao.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-10-23
 *         Time: 下午5:44
 *         To change this template use File | Settings | File Templates.
 */
public class WordFinder {
    public static void main(String[] args) {
        String domain = "weibo.cn/pages";
        String url = "http://"+domain+"/2" ;
        String url2 = "http://"+domain+"/1323434g.sdfe/?ttt=1" ;
        String url3 = "http://"+domain+"/13234te_erdf.+ef-f/ttt" ;
        String url4 = "http://"+domain+"/100101116.356063_40.034656/?ggg" ;
        String pattern = domain+"/([^/\\?]+)[/\\?]?";    //  "/([\\w\\d?]+)[/\\?]?"
        System.out.println(findWordInGroup(url,pattern));
        System.out.println(findWordInGroup(url2, pattern));
        System.out.println(findWordInGroup(url3, pattern));
        System.out.println(findWordInGroup(url4, pattern));
     }

    public static String findWordInGroup(String sentence, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(sentence);
        if(m.find() && m.groupCount()>0)
            return m.group(1);
        return "";
    }
}
