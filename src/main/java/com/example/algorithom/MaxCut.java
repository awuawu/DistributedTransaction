package com.example.algorithom;

/*
* 将一个正整数至少分割成两部分，使得乘积最大
* */

//f(n) = max(f(n-1)*1,f(n-2)*2,...,f(1)*(n-1),(n-1)*1,(n-2)*2,...,1*(n-1))
public class MaxCut {
    //递归
    public static int cut(int n){//分割n产生的最大值
        int max = Integer.MIN_VALUE;

        //基本问题
        if(n==4)
            return 4;
        if(n==3)
            return 2;
        if(n==2)
            return 1;
        if(n==1)
            return 1;

        //所有子问题
        for (int i = 1; i < n; i++) {
            int twoPart = i*(n-i);//只把n分割成i和n-i两部分
            int childPartMax = cut(n-i);//对n-i继续分割产生一个最大值
            int temp = Math.max(twoPart, i*childPartMax);
            //根据子问更新当前问题
            max = temp>max?temp:max;
        }
        return max;
    }

    //记忆化
    public static int cutMind(int n,int[] memo){
        int max = Integer.MIN_VALUE;

        //子问题计算过直接返回
        if(memo[n]!=0)
            return memo[n];

        //基本问题
        if(n==4)
            return 4;
        if(n==3)
            return 2;
        if(n==2)
            return 1;
        if(n==1)
            return 1;

        //所有子问题
        for (int i = 1; i < n; i++) {
            int twoPart = i*(n-i);//只把n分割成i和n-i两部分
            int childPartMax = cutMind(n-i,memo);//对n-i继续分割产生一个最大值

            //记录子问题
            memo[n-i] = childPartMax;
            int temp = Math.max(twoPart, i*childPartMax);

            //根据子问更新当前问题
            max = temp>max?temp:max;
        }
        return max;
    }

    //记忆化
    public static int cutDP(int n){
        int[] dp = new int[n+1];

        //基本问题初始化
        dp[1]=1;dp[2]=1;dp[3]=2;dp[4]=4;

        //迭代
        for (int i = 5; i <=n ; i++) {
            int max = Integer.MIN_VALUE;

            //状态转移
            for (int j = 1; j < i; j++) {
                int twoPart = j*(i-j);
                int childPartMax = dp[i-j];
                int temp = Math.max(twoPart, j*childPartMax);
                max = temp>max?temp:max;
            }
            dp[i] = max;
        }
        return dp[n];
    }

    public static void main(String[] args) {
        long s1 = System.nanoTime();
        System.out.println(cut(10)+" cost "+(System.nanoTime()-s1));
        int[] memo = new int[11];

        long s2 = System.nanoTime();
        System.out.println(cutMind(10,memo)+" cost "+(System.nanoTime()-s2));

        long s3 = System.nanoTime();
        System.out.println(cutDP(10)+" cost "+(System.nanoTime()-s3));
    }
}
