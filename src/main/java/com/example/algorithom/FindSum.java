package com.example.algorithom;

public class FindSum {
    //递归
    public static boolean isContain(int[] num,int s,int e,int r){//在num数组中从位置s开始e结束你能否找到和等于r的一组数
        if(s==e){
            return num[s]==r;
        }
        return isContain(num,s+1,e,r-num[s])    //用了第s个元素
                || isContain(num,s+1,e,r);         //不用第s个元素
    }


    public static void main(String[] args) {
        int[] num = new int[]{1,2,3,4,5};
        System.out.println(isContain(num,0,num.length-1,7));
    }
}
