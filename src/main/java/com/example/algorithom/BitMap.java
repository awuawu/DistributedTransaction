package com.example.algorithom;

import java.util.HashMap;
import java.util.Map;

public class BitMap {
    public static int numMax(int[] nums){
        Map<Integer,Integer> bitMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer cur = bitMap.get(nums[i]);
            bitMap.put(nums[i],cur==null?1:cur+1);
        }
        int max = Integer.MIN_VALUE;
        int num = Integer.MAX_VALUE;
        for(Integer key:bitMap.keySet()){
            if(bitMap.get(key)>max){
                max = bitMap.get(key);
                num = key;
            }
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(numMax(new int[]{1,1,2,2,2,-3,-3,-3,3,3,3,3}));
    }
}
