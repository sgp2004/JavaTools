package com.daodao.utils.base62;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-6-6
 *         Time: 上午11:18
 *         To change this template use File | Settings | File Templates.
 */
public class Base62Utils {

    private static final String seed = "vPh7zZwA2LyU4bGq5tcVfIMxJi6XaSoK9CNp0OWljYTHQ8REnmu31BrdgeDkFs";
    private static final long base = 62;

    public static String base62Encode(final long id) {
        final StringBuilder sb = new StringBuilder();
        long num = id;
        int value;
        while (num > 0) {
            value = (int) (num % base);
            num = num / base;
            sb.append(seed.charAt(value));
        }

        return sb.reverse().toString();
    }

    public static long base62Decode(final String str) throws Exception{
        long num = 0;
        int pos;
        char c;
        for (int i = 0; i < str.length(); ++i) {
            c = str.charAt(i);
            pos = seed.indexOf(c);
            if (pos < 0) {
                throw new Exception("ExcepFactor.E_SINAURL_WRONG_PARAM_VALUE");
            }
            num = num * base + pos;
        }
        return num;
    }

    /**
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        System.out.println(base62Decode("zA94pDvYk")); // 20 + 40195082
        System.out.println(base62Decode("aNvKqn"));
        System.out.println(base62Encode(201135115602l));
        System.out.println(base62Encode(39682435l));
        long t = 899840856713097l;
    }

}