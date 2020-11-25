package com.example.algorithom;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//1,5,3,2,6,10,8
public class Wangyi {
    public static Lock lockA = new ReentrantLock();
    public static Lock lockB = new ReentrantLock();
    public static Lock lockC = new ReentrantLock();
    public static Condition conA = lockA.newCondition();
    public static Condition conB = lockB.newCondition();
    public static Condition conC = lockC.newCondition();

    public static int findGeMax(int[] nums,int end){
        if(nums==null||end<0)
            return 0;
        if(nums.length==1||end==0)
            return nums[0];
        if(nums.length==2||end==1)
            return Math.max(nums[0],nums[1]);
        int use = findGeMax(nums,end-2)+nums[end];
        int unUse = findGeMax(nums,end-1);
        return 0;
    }

    //dp[i] = Math.max((dp[i-2] + num[i]),dp[i-1])

    public static void main(String[] args) {
        int[] nums = new int[]{1,5,3,2,6,10,8};
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0],nums[1]);
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i-2]+nums[i],dp[i-1]);
        }
        System.out.println(dp[nums.length-1]);
    }
}
