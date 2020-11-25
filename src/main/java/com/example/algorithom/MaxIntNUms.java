package com.example.algorithom;

public class MaxIntNUms {
    public static void main(String[] args) {
        int[] nums = new int[]{1,2,3,-1,-10,7};
        int cur = 0;
        int nxt = 0;
        for (int i = 0; i < nums.length; i++) {
            if(nums[i]>0){
                int three = nxt+nums[i];
                cur = findMax(cur,nums[i],three);
            }
            nxt+=nums[i];
        }
        System.out.println(cur);
    }

    private static int findMax(int cur, int num, int three) {
        return Math.max(cur,Math.max(num,three));
    }
}
