package com.daodao.tmp;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-12-24
 *         Time: 下午7:35
 *         To change this template use File | Settings | File Templates.
 */
public class StatusTest {


    private List<AggregatorEntry> statuses = new ArrayList<AggregatorEntry>(1);

    public  void add(AggregatorEntry aggregatorEntry){
        statuses.add(aggregatorEntry);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (AggregatorEntry aggregatorEntry :statuses)
            sb.append(aggregatorEntry.uid+"");
        return sb.toString();
    }


    public static class AggregatorEntry {
        public static long INVALID_ID = -1;

        private long uid; // �ۺ��û������
        private long filter; // ����Ĺ��˵�����ֵ

        public AggregatorEntry(long uid, long filter) {
            this.uid = uid;
            this.filter = filter;
        }
    }


    public static void main(String[] args) {
        StatusTest statusTest = new StatusTest();
        statusTest.add(new AggregatorEntry(1l,2l));
        statusTest.add(new AggregatorEntry(2l,3l));
        System.out.println(statusTest.toString());


        System.out.println(URI.create("http://www.weibo.com/ab/").getHost());
    }

}
