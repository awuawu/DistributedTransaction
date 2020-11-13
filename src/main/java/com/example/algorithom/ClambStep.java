package com.example.algorithom;
/*
* 有n阶阶梯，每次只能爬一步或者两步，有多少种爬法
* */
//f(n) = f(n-1) + f(n-2)
public class ClambStep {
    //递归
    public static int clambNum(int n){//爬上n阶的爬法
        //基本问题
        if(n==1)
            return 1;
        if(n==2)
            return 2;

        //子问题
        int n1 = clambNum(n-1);
        int n2 = clambNum(n-2);

        //由子问题得到当前问题
        return n1+n2;
    }

    //记忆化
    public static int clambNumMind(int n,int[] memo){//memo[i]保存爬到第i步阶梯的方法数
        //子问题计算过直接返回
        if(memo[n]!=0)
            return memo[n];

        //基本问题
        if(n==1)
            return 1;
        if(n==2)
            return 2;

        //子问题
        int n1 = clambNumMind(n-1,memo);
        int n2 = clambNumMind(n-2,memo);

        //记录子问题
        memo[n] = n1 + n2;
        return memo[n];
    }

    //动态规划
    public static int clambNumDP(int n){
        int[] dp = new int[n+1];

        //基本问题
        dp[1] = 1;dp[2] = 2;

        //迭代
        for (int i = 3; i <=n ; i++)
            dp[i] = dp[i-1]+dp[i-2];//状态转移方程
        return dp[n];
    }

    public static void main(String[] args) {
        long s1 = System.nanoTime();
        System.out.println(clambNum(44)+" cost "+(System.nanoTime()-s1));

        int[] memo = new int[45];
        long s2 = System.nanoTime();
        System.out.println(clambNumMind(44,memo)+" cost "+(System.nanoTime()-s2));

        long s3 = System.nanoTime();
        System.out.println(clambNumDP(44)+" cost "+(System.nanoTime()-s3));
    }
}
