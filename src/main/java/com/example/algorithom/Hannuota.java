package com.example.algorithom;

/*
* A B C
* */

//f(n,from,to,help) = f(n-1,from,help,to) + 1 + f(n-1,help,to,from)
public class Hannuota {
    public static int move(int n,String from,String to,String help,int step){//将n从from移动到to时，辅助是help
        //基本问题
        if(n==2){
            System.out.println("step"+(step+1)+": "+"move 1 from "+from+" to "+help);
            System.out.println("step"+(step+2)+": "+"move 2 from "+from+" to "+to);
            System.out.println("step"+(step+3)+": "+"move 1 from "+help+" to "+to);
            return 3;
        }
        if(n==1){
            System.out.println("step"+(step+1)+": "+"move 1 from "+from+" to "+to);
            return 1;
        }

        //子问题
        int childpre = move(n-1,from,help,to,step);//先将n-1从from移动到help,用to作为辅助
        System.out.println("step"+(step+childpre+1)+": "+"move "+n+" from "+from+" to "+to);//将n从from移动到to
        int childend = move(n-1,help,to,from,step+childpre+1);//后将n-1从help移动到to,用from作为辅助

        //由子问题得到当前问题
        return childpre+1+childend;
    }

    //记忆化
    public static int moveMind(int n,String from,String to,String help,int step,int[] memo){//将n从from移动到to时，辅助是help
        //子问题计算过直接返回
        if(memo[n]!=0)
            return memo[n];

        //基本问题
        if(n==1){
            return 1;
        }

        //子问题
        int childpre = moveMind(n-1,from,help,to,step,memo);//先将n-1从from移动到help,用to作为辅助
        int mid = 1;//将n从from移动到to
        int childend = moveMind(n-1,help,to,from,step+childpre+1,memo);//后将n-1从help移动到to,用from作为辅助

        //记录子问题
        memo[n] = childpre+mid+childend;
        return memo[n];
    }

    //动态规划
    public static int moveDP(int n){
        int[] dp = new int[n+1];

        //基本问题初始化
        dp[1] = 1;

        //迭代
        for (int i = 2; i <=n ; i++)
            dp[i] = 2*dp[i-1]+1;//状态转移方程
        return dp[n];
    }

    public static void main(String[] args) {
        long s1 = System.nanoTime();
        System.out.println(move(11,"A","C","B",0)+" cost "+(System.nanoTime()-s1));
        int[] memo = new int[12];

        long s2 = System.nanoTime();
        System.out.println(moveMind(11,"A","C","B",0,memo)+" cost "+(System.nanoTime()-s2));

        long s3 = System.nanoTime();
        System.out.println(moveDP(11)+" cost "+(System.nanoTime()-s3));
    }
}
