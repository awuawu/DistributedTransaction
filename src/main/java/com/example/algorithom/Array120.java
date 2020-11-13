package com.example.algorithom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//[
//        [2],
//       [3,4],
//      [6,5,7],
//     [4,1,8,3]
//        ]

public class Array120 {
    public static int minimumTotal(List<List<Integer>> triangle) {
//        return caculate(triangle,0,0);
        int[][] memo = new int[triangle.size()][triangle.size()];
        return caculateMind(triangle,0,0,memo);
    }

    public static int caculate(List<List<Integer>> triangle,int level,int location) {
        if(level==triangle.size()-1)
            return triangle.get(level).get(location);
        int cur = triangle.get(level).get(location);
        int left = caculate(triangle,level+1,location);
        int right = caculate(triangle,level+1,location+1);
        return left<right?(cur+left):(cur+right);
    }

    public static int caculateMind(List<List<Integer>> triangle,int level,int location,int[][] memo) {
        if(memo[level][location]!=0)
            return memo[level][location];
        if(level==triangle.size()-1) {
            memo[level][location] = triangle.get(level).get(location);
            return memo[level][location];
        }
        int cur = triangle.get(level).get(location);
        int left = caculateMind(triangle,level+1,location,memo);
        int right = caculateMind(triangle,level+1,location+1,memo);
        memo[level][location] = left<right?(cur+left):(cur+right);
        return memo[level][location];
    }

    public static void main(String[] args) {
        List<Integer> l1 = Arrays.asList(2);
        List<Integer> l2 = Arrays.asList(3,4);
        List<Integer> l3 = Arrays.asList(6,5,7);
        List<Integer> l4 = Arrays.asList(4,1,8,3);
        List<List<Integer>> triangle = Arrays.asList(l1,l2,l3,l4);

        System.out.println(minimumTotal(triangle));
    }
}
