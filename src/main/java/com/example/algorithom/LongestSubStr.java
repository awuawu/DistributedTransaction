package com.example.algorithom;

public class LongestSubStr {
    public static String subStr(String s1,String s2,int from,int to){
        if(s2.length()==1){
            return s1.contains(s2.substring(0))?s2.substring(0):null;
        }
        if(from+1==to){
            return s1.contains(s2.substring(from,to))?s2.substring(from,to):null;
        }
        if(s1.contains(s2.substring(from,to)))
            return s2.substring(from,to);
        return handle(subStr(s1,s2,from+1,to),subStr(s1,s2,from,to-1));

    }

    private static String handle(String s1, String s2) {
        if(s1==null&&s2==null)
            return null;
        if(s1!=null||s2!=null){
            if(s1!=null&&s2!=null){
                return s1.length()>s2.length()?s1:s2;
            }
            if(s1!=null)
                return s1;
            if(s2!=null)
                return s2;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(subStr("abadce","badce",0,5));
    }
}
