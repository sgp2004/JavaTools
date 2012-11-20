package com.daodao.utils.sets;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 12-10-8
 *         Time: 下午6:58
 *         To change this template use File | Settings | File Templates.
 */
public class SetParty {
    public static <T> T[] excludeIdsInSet(T[] ids, Set<T> idsSet) {
        List<T> list = new CopyOnWriteArrayList<T>(Arrays.asList(ids));
        for(T id :list) {
            if(idsSet.contains(id))
                list.remove(id);
        }
        return (T[]) Arrays.copyOf(list.toArray(), list.size(),ids.getClass());
    }

    public static <T> T[] excludeDuplicateIds(T[] ids){
        List<T> list = new ArrayList<T>();
        for (T objectId : ids) {
            if (list.contains(objectId))
                continue;
            list.add(objectId);
        }
        return  (T[]) Arrays.copyOf(list.toArray(), list.size(),ids.getClass());
    }


    public static void main(String[] args) {
       String[] ids = excludeDuplicateIds(new String[]{"1","3","1"});
       System.out.println("ids size:"+ids.length);
       for (String id:ids)
           System.out.println(id);

       Long[] longs = excludeDuplicateIds(new Long[]{1l,2l,3l,2l,1l});
       System.out.println("longs size:"+longs.length);
       for (Long id:longs)
           System.out.println(id);

        Integer[] ints = excludeDuplicateIds(new Integer[]{1,2,3,4,2,3,4});
        System.out.println("ints size:"+ints.length);
        for (Integer id:ints)
            System.out.println(id);

       ids = excludeIdsInSet(new String[]{"1","3","1"},new HashSet<String>(Arrays.asList(new String[]{"1","3","1"})));
        System.out.println("ids size:"+ids.length);
        for (String id:ids)
            System.out.println(id);
    }
}
