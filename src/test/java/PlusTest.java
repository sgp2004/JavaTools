import sun.misc.BASE64Decoder;

public class PlusTest{
    public static void main(String[] args){
        int hash = 2;
        System.out.println(hash & 2);
        System.out.println(hash(2));
        System.out.println(0xffffcd7d);

        String encode=getBASE64("out004");    //fanxer_facai
        System.out.println(encode);
        System.out.println(getFromBASE64(encode));;

       Integer t = null;


}
    private static int hash(int h) {
        // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
        h += (h <<  15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h <<   3);
        h ^= (h >>>  6);
        h += (h <<   2) + (h << 14);
        return h ^ (h >>> 16);
    }

    public static String getBASE64(String s) {
        if (s == null) return null;
        return (new sun.misc.BASE64Encoder()).encode( s.getBytes() );
    }

    public static String getFromBASE64(String s) {
        if (s == null) return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
}
