package com.example.algorithom;

/*
* wi表示每个商品的重量，vi表示每个商品的价值，怎么选择商品使得重量不超过h而价值最大
* */

//f(w,v,e,h) = max(v[e]+f(w,v,e-1,h-w[e]),f(w,v,e-1,h))
public class Bag01 {
    //递归
    public static int bag(int[] w,int[] v,int e,int h){//用前e个物品装容量为h的包产生的最大价值
        //基本问题
        if(e==0)
            return w[0]<=h?v[0]:0;

        //子问题
        int useE = w[e]<=h?(v[e]+bag(w,v,e-1,h-w[e])):bag(w,v,e-1,h);
        int unUseE = bag(w,v,e-1,h);

        //由子问题得到当前问题
        return useE>unUseE?useE:unUseE;
    }

    //记忆化
    public static int bagMind(int[] w,int[] v,int e,int h,int[][] memo){
        //已经计算的子问题直接返回结果
        if(memo[e][h]!=0)
            return memo[e][h];

        //基本问题
        if(e==0)
            return w[0]<=h?v[0]:0;

        //子问题
        int useE = w[e]<=h?(v[e]+bagMind(w,v,e-1,h-w[e],memo)):bagMind(w,v,e-1,h,memo);
        int unUseE = bagMind(w,v,e-1,h,memo);

        //记录子问题
        memo[e][h] = useE>unUseE?useE:unUseE;
        return memo[e][h];
    }

    //动态规划
    public static int bagDP(int[] w,int[] v,int c){
        int[][] dp = new int[w.length][c+1];

        //基本问题
        for (int i = 0; i < dp[0].length; i++)
            dp[0][i] = i>=w[0]?v[0]:0;

        //迭代
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                //状态转移方程
                if(j<w[i])
                    dp[i][j] = dp[i-1][j];
                else
                    dp[i][j] = Math.max(dp[i-1][j],v[i]+dp[i-1][j-w[i]]);
            }
        }
        return dp[w.length-1][c];
    }


    public static void main(String[] args) {
        int[] w1 = new int[]{77,22,29,50,99};
        int[] v1 = new int[]{92,22,87,46,90};
        int c1 = 100;
        int[] w2 = new int[]{79,58,86,11,28,62,15,68};
        int[] v2 = new int[]{83,14,54,79,72,52,48,62};
        int c2 = 200;
        int[] w3 = new int[]{95,75,23,73,50,22,6,57,89,98};
        int[] v3 = new int[]{89,59,19,43,100,72,44,16,7,64};
        int c3 = 300;

        long s1 = System.nanoTime();
        System.out.println(bag(w3,v3,w3.length-1,c3)+" cost "+(System.nanoTime()-s1));

        int[][] memo = new int[w3.length][c3+1];
        long s2 = System.nanoTime();
        for (int i = 0; i < memo[0].length; i++)
            memo[0][i] = i>=w3[0]?v3[0]:0;
        System.out.println(bagMind(w3,v3,w3.length-1,c3,memo)+" cost "+(System.nanoTime()-s2));

        long s3 = System.nanoTime();
        System.out.println(bagDP(w3,v3,c3)+" cost "+(System.nanoTime()-s3));
    }
}
