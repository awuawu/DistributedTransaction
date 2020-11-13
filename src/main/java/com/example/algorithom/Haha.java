package com.example.algorithom;

public class Haha {
    //找出相同字符长度的最大值，aba
    public static int fun(String str){
        return funFind(str,0,str.length()-1);
    }

    public static int funFind(String str, int start, int end) {
        if(str==null||str.length()<=2||end-start<=1)
            return 0;
        if(str.charAt(start)==str.charAt(end))
            return end-start-1;

        int left = funFind(str,start+1,end);
        int right = funFind(str,start,end-1);
        return left>right?left:right;
    }

    public static void main(String[] args) {
        String str = "baacbbbaa";
        int[] lo = new int[1000];
        int[] ll = new int[1000];
        int max = 0;
//        System.out.println(fun("baacbbba"));
        for (int i = 0,j=str.length()-1; i < str.length(); i++,j--) {
            if(lo[str.charAt(i)]==0)
                lo[str.charAt(i)]=i+1;
            if(ll[str.charAt(j)]==0)
                ll[str.charAt(j)]=j+1;
        }
        for (int i = 0; i < 1000; i++) {
            max = Math.max(max,Math.abs(lo[i]-ll[i]));
        }
        System.out.println(max);
    }
}
