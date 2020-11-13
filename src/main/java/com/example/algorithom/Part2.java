package com.example.algorithom;

public class Part2 {
    public static int find(int[] nums,int from, int to,int x){
        if(from==to){
            if(nums[to]>x)
                return nums[to];
            else
                return Integer.MIN_VALUE;
        }
        int mid = (from+to)/2;
        if(x==nums[mid]){
            int i = mid+1;
            while (nums[i]==x)
                i++;
            return nums[i];
        }else if(x>nums[mid]){
            return find(nums,mid+1,to,x);
        }else{
            return find(nums,from,mid,x);
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,22,333,333,555,777};
        System.out.println(find(nums,0,nums.length-1,777));
    }
}
