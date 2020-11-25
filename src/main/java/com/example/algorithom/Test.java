package com.example.algorithom;

//String1+String2
public class Test {
    public static String addWithPoint(String s1,String s2){
        String[] nums1 = s1.split(".");
        String[] nums2 = s2.split(".");
        return adds(nums1[0],nums2[0])+"."+adds(nums1[1],nums2[1]);
    }

    private static String adds(String s1, String s2) {
        int start = s1.length()>s2.length()?s2.length()-1:s1.length()-1;
        StringBuffer res = new StringBuffer();
        int[] step = new int[]{0,0};
        for (int i = start; i >=0 ; i--) {
            step = addStep(s1.charAt(i),s2.charAt(i),step[1]);
            res.append(step[0]);
        }

        return res.toString();
    }

    private static int[] addStep(int num1,int num2,int step) {
        int[] res = new int[2];
        res[0] = (num1+num2+step)/10;
        res[0] = (num1+num2+step)%10;
        return res;
    }


}
