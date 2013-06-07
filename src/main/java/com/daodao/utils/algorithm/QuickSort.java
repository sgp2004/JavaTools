package com.daodao.utils.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-3-11
 *         Time: 上午8:59
 *         To change this template use File | Settings | File Templates.
 */
public class QuickSort <T extends String>{
    public static void main(String[] args) {
        int[] array = {10,5,8,20,3,9};
        System.out.println("start"+Arrays.toString(array));
        quickSort(array, 0, array.length - 1);
        System.out.println("after sorting:"+ Arrays.toString(array));
    }

    private static void quickSort(int[] array,int low,int high) {
        if(low<high)       {
            int loc = partition(array, low, high);
            quickSort(array,low,loc-1);
            quickSort(array,loc+1,high);
        }

    }

    private static int partition(int[] array, int low, int high) {
       // int index = (low+high)/2;
        int key = array[low];
        System.out.println("key:"+key);
        while (low<high){
            while (low<high && array[high]>=key) --high;
            swap(array,high,low);
            System.out.println(Arrays.toString(array));
            System.out.println("high="+high);
            while (low<high && array[low]<=key) ++low;
            swap(array,high,low);

            System.out.println(Arrays.toString(array));
            System.out.println("low="+low);
        }
        System.out.println("after one sort:"+Arrays.toString(array));

        Set<String> set = new HashSet<String>();
        QuickSort<String> quickSort = new QuickSort<String>();
        quickSort.addSet(set,"abc");
        return low;
    }

    private   void addSet(Set<String> set, String abc) {
        set.add(null);
        set.add(abc);
       // return "";
    }

    private static void swap(int[] array, int high, int index) {
        int tmp;
        tmp = array[high];
        array[high] = array[index];
        array[index] = tmp;
    }
}
