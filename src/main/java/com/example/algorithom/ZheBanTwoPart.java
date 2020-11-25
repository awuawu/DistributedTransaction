package com.example.algorithom;

public class ZheBanTwoPart {
    public static int findFen(int[] nums,int start,int end){
        int mid = (start+end)/2;
        if(nums[mid]>nums[mid+1])
            return mid+1;
        if(nums[mid]<nums[mid-1])
            return mid;
        if(nums[mid]<nums[end])
            return findFen(nums,start,mid);
        if(nums[mid]>nums[end])
            return findFen(nums,mid,end);
        return Integer.MAX_VALUE;
    }

    public static int findIndex(int[] nums,int num){
        int fenge = findFen(nums,0,nums.length-1);
        if(num<nums[fenge])
            return -1;
        else if(num<=nums[nums.length-1])
            return twoFin(nums,num,fenge,nums.length-1);
        else if(num<nums[0])
            return -1;
        else if(num<=nums[fenge-1])
            return twoFin(nums,num,0,fenge-1);
        else
            return -1;
    }

    private static int twoFin(int[] nums, int num, int start, int end) {
        if(nums==null||nums.length==0||start>end)
            return -1;
        if(nums.length==1)
            return num==nums[0]?0:-1;
        if (start==end)
            return num==nums[end]?end:-1;
        int mid = (start+end)/2;
        if(num==nums[mid])
            return mid;
        if(num>nums[mid])
            return twoFin(nums,num,mid+1,end);
        if(num<nums[mid])
            return twoFin(nums,num,start,mid);
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(findIndex(new int[]{3,4,5,6,1,2},6));
    }
}
