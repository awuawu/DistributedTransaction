package com.example.algorithom;

//给定一个包含非负整数的 m x n 网格，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
//
//        说明：每次只能向下或者向右移动一步。

//输入:
//        [
//        [1,3,1],
//        [1,5,1],
//        [4,2,1]
//        ]
//        输出: 7
//        解释: 因为路径 1→3→1→1→1 的总和最小。


//f(g,i,j) = g(i,j) + min(f(i+1,j),f(i,j+1))
class MatrixMinRoad {
    public static int minPathSum(int[][] grid) {
        int[][] memo = new int[grid.length][grid[0].length];
        return caculateMind(grid,0,0,memo);
    }

    //递归
    public static int caculate(int[][] grid, int i, int j) {//从（i,j）到右下角的最短路径
        //基本问题
        if(i==grid.length-1){
            int sum = 0;
            for (int k = j; k < grid[0].length; k++)
                sum+=grid[i][k];
            return sum;
        }
        if(j==grid[0].length-1){
            int sum = 0;
            for (int k = i; k < grid.length; k++)
                sum+=grid[k][j];
            return sum;
        }

        //子问题
        int next = caculate(grid,i+1,j);//从（i,j）的右边到右下角的最短路径
        int right = caculate(grid,i,j+1);//从（i,j）的下边到右下角的最短路径

        //由子问题得到当前问题
        return next<right?(grid[i][j]+next):(grid[i][j]+right);
    }

    //记忆化
    public static int caculateMind(int[][] grid, int i, int j, int[][] memo) {//memo[i][j]表示从（i,j）走到右下角的最短路径
        //子问题计算过直接返回
        if(memo[i][j]!=0)
            return memo[i][j];

        //基本问题
        if(i==grid.length-1){
            int sum = 0;
            for (int k = j; k < grid[0].length; k++)
                sum+=grid[i][k];
            memo[i][j] = sum;//记录子问题
            return sum;
        }
        if(j==grid[0].length-1){
            int sum = 0;
            for (int k = i; k < grid.length; k++)
                sum+=grid[k][j];
            memo[i][j] = sum;//记录子问题
            return sum;
        }

        //子问题
        int next = caculateMind(grid,i+1,j,memo);
        int right = caculateMind(grid,i,j+1,memo);

        //记录子问题
        memo[i][j] = next<right?(grid[i][j]+next):(grid[i][j]+right);
        return memo[i][j];
    }

    //动态规划
    public static int caculateDP(int[][] grid){//grid[i][j]表示到位置i,j的最短路径
        //基本问题，此处不用初始化
        //迭代
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                //状态转移
                if(i == 0 && j == 0) continue;
                else if(i == 0)  grid[i][j] = grid[i][j - 1] + grid[i][j];//在下上边界上，只能从左边过来
                else if(j == 0)  grid[i][j] = grid[i - 1][j] + grid[i][j];//在左边界上，只能从上面过来
                else grid[i][j] = Math.min(grid[i - 1][j], grid[i][j - 1]) + grid[i][j];//取从上面来和左边来的最小值
            }
        }
        return grid[grid.length - 1][grid[0].length - 1];
    }

    public static void main(String[] args) {
        int[][] grid = new int[][]{{1,3,1,9,1,2},{1,5,2,8,2,3},{4,2,3,7,3,4},{1,3,4,6,4,5},{1,5,5,5,5,6},{4,2,6,4,6,7},{1,3,7,3,7,8},{1,5,8,2,8,9},{4,2,1,1,1,2}};
        long s1 = System.nanoTime();
        System.out.println(caculate(grid,0,0)+" cost "+(System.nanoTime()-s1));

        long s2 = System.nanoTime();
        System.out.println(minPathSum(grid)+" cost "+(System.nanoTime()-s2));

        long s3 = System.nanoTime();
        System.out.println(caculateDP(grid)+" cost "+(System.nanoTime()-s3));


    }
}
