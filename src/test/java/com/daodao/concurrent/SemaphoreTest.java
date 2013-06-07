package com.daodao.concurrent;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-1-3
 *         Time: 下午5:38
 *         To change this template use File | Settings | File Templates.
 */
public class SemaphoreTest {
    class BoundedHashSet<T> {
        private final Set<T> set;
        private final Semaphore sem;

        public BoundedHashSet(int bound) {
            this.set = Collections.synchronizedSet(new HashSet<T>());
            sem = new Semaphore(bound);
        }

        public boolean add(T o) throws InterruptedException {
            sem.acquire();
            boolean wasAdded = false;
            try {
                wasAdded = set.add(o);
                System.out.println("BoundedHashSet size=" + set.size());
                return wasAdded;
            } finally {
                if (!wasAdded)
                    sem.release();
            }
        }

        public boolean remove(Object o) {
            boolean wasRemoved = set.remove(o);
            if (wasRemoved)
                sem.release();
            return wasRemoved;
        }
    }
    public void test() throws InterruptedException {
        BoundedHashSet<Integer> boundedHashSet = new BoundedHashSet<Integer>(15);
        for(int i=0;i<10;i++){
            boundedHashSet.add(new Integer(i)) ;
        }
        for(int i=0;i<10;i++){
            boundedHashSet.remove(new Integer(i)) ;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new SemaphoreTest().test();
        System.out.println("{\"id\":\"1032:134\",\"author\":{\"id\":\"1593@5824\",\"display_name\":\"厂商供稿\",\"url\":\"http://view.sina.com/url/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vem9uZ2hlLzIwMTMwMTA0LzExMDU5NzYxODIuaHRtbA==&k=1dfc7&f=va\",\"object_type\":\"person\"},\"display_name\":\"北现获中国汽车总评榜\"年度风云汽车品牌\"\",\"object_type\":\"collection\",\"image\":{\"url\":\"http:\\/\\/r3.sinaimg.cn\\/201301\\/300\\/300\\/aHR0cDovL2ltZzMuYml0YXV0b2ltZy5jb20vYml0YXV0by8vMjAxMy8wMS8xMDU4MjU0OTcuanBnK2h0dHA6Ly9uZXdzLmJpdGF1dG8uY29tL3pvbmdoZS8yMDEzMDEwNC8xMTA1OTc2MTgyLmh0bWw=.jpg\",\"width\":\"50\",\"height\":\"50\"},\"url\":\"http:\\/\\/apps.e.weibo.com\\/mediapublic\\/atomlist?stuff_id=134\",\"object_types\":[\"article\"],\"items\":[{\"id\":\"1593@5824\",\"display_name\":\"北现获中国汽车总评榜\"年度风云汽车品牌\"\",\"summary\":\"12月22日，由中国主流媒体汽车联盟主办，“驱动2013———...\",\"url\":\"http:\\/\\/view.sina.com\\/url\\/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vem9uZ2hlLzIwMTMwMTA0LzExMDU5NzYxODIuaHRtbA==&k=1dfc7&f=va\"},{\"id\":\"1593@5823\",\"display_name\":\"采用新的家族设计 全新蒙迪欧或5月上市\",\"summary\":\"日前从相关渠道获悉，长安福特全新蒙迪欧有望在今年5...\",\"url\":\"http:\\/\\/view.sina.com\\/url\\/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20veGluY2hlLzIwMTMwMTA0LzEwMDU5NzYxNTEuaHRtbA==&k=bd278&f=va\"},{\"id\":\"1593@5821\",\"display_name\":\"美丽中国  开瑞汽车为你开启美好生活\",\"summary\":\"在中共十八大后，胡锦涛的“美丽中国”和习近平的“美好...\",\"url\":\"http:\\/\\/view.sina.com\\/url\\/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vb3RoZXJzLzIwMTMwMTA0LzEwMDU5NzYxMDYuaHRtbA==&k=ab1cf&f=va\"},{\"id\":\"1593@5820\",\"display_name\":\"1月9日即将上市 淮安3008接受预订\",\"summary\":\"城市 到店时间 商品车数量 颜色 订金提车周期 有无试...\",\"url\":\"http:\\/\\/view.sina.com\\/url\\/?u=aHR0cDovL25ld3MuYml0YXV0by5jb20vZGFvZGlhbi8yMDEzMDEwNC8xMDA1OTc2MTA4Lmh0bWw=&k=9327c&f=va\"}],\"create_at\":\"January 4, 2013, 10:55 am\",\"updated\":\"January 4, 2013, 10:55 am\"}".length());
    }
}
