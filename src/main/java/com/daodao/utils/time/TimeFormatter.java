package com.daodao.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-10-8
 *         Time: 下午6:48
 *         To change this template use File | Settings | File Templates.
 */
public class TimeFormatter {
    public static Date parseStandUsDateFromString(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date result = null;
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            result = new Date();     //if time error just use datetime now
        }
        return result;
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println(parseStandUsDateFromString(new Date().toString()));
        System.out.println(parseStandUsDateFromString(""));
        long[] ids = new long[2];
        System.out.println(ids[1]);

    }
}
