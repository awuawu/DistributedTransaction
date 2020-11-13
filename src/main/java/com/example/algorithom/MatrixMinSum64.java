package com.example.algorithom;

//输入:
//        [
//        [1,3,1],
//        [1,5,1],
//        [4,2,1]
//        ]
//        输出: 7
//        解释: 因为路径 1→3→1→1→1 的总和最小。

import java.util.List;

public class MatrixMinSum64 {
    public static int minPathSum(int[][] grid) {
//        return caculate(grid,0,0);
        int[][] memo = new int[grid.length][grid[0].length];
        return caculateMind(grid,0,0,memo);
    }

    public static int caculate(int[][] grid, int i, int j) {
        if(i==grid.length-1){
            int sum = 0;
            for (int k = j; k < grid[0].length; k++) {
                sum+=grid[i][k];
            }
            return sum;
        }
        if(j==grid[0].length-1){
            int sum = 0;
            for (int k = i; k < grid.length; k++) {
                sum+=grid[k][j];
            }
            return sum;
        }


        int cur = grid[i][j];
        int next = caculate(grid,i+1,j);
        int right = caculate(grid,i,j+1);
        return next<right?(cur+next):(cur+right);
    }

    public static int caculateMind(int[][] grid, int i, int j, int[][] memo) {
        if(memo[i][j]!=0)
            return memo[i][j];
        if(i==grid.length-1){
            int sum = 0;
            for (int k = j; k < grid[0].length; k++) {
                sum+=grid[i][k];
            }
            memo[i][j] = sum;
            return sum;
        }
        if(j==grid[0].length-1){
            int sum = 0;
            for (int k = i; k < grid.length; k++) {
                sum+=grid[k][j];
            }
            memo[i][j] = sum;
            return sum;
        }


        int cur = grid[i][j];
        int next = caculateMind(grid,i+1,j,memo);
        int right = caculateMind(grid,i,j+1,memo);
        memo[i][j] = next<right?(cur+next):(cur+right);
        return memo[i][j];
    }

    public static void main(String[] args) {
        int[][] grid = new int[][]{{1,3,1},{1,5,1},{4,2,1}};
        System.out.println(minPathSum(grid));
    }
}
