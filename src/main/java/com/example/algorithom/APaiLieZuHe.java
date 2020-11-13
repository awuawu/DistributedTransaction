package com.example.algorithom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APaiLieZuHe {
    //有e组数，组内有序，组间无序的排列组合方式
    public static List<List<Integer>> pailieN(int[][] nums,int e){
        if(nums.length==2||e==1)
            return pailie2(nums[0],nums[1],nums[0].length-1);
        return wrapN(pailieN(nums,e-1),nums[e]);
    }

    //新来一组数，包裹到原排列上
    private static List<List<Integer>> wrapN(List<List<Integer>> pailieN, int[] num) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < pailieN.size(); i++) {
            int[] arr = pailieN.get(i).stream().mapToInt(Integer::valueOf).toArray();
            List<List<Integer>> temp = pailie2(num,arr,num.length-1);
            res.addAll(temp);
         }
        return res;
    }

    //n1,n2排列，内部有序，外部无序，考虑n1的前e1个元素
    public static List<List<Integer>> pailie2(int[] n1,int[] n2,int e1){
        List<List<Integer>> res = new ArrayList<>();
        if(n1==null||n2==null||e1<0)
            return null;
        if(n1.length==1||e1==0){
            for (int j = 0; j <= n2.length; j++) {
                List<Integer> temp = new ArrayList<>();
                for (int k = 0; k < n2.length; k++) {
                    if(j==k)
                        temp.add(n1[0]);
                    temp.add(n2[k]);
                    if(k==n2.length-1&&j== n2.length)
                        temp.add(n1[0]);
                }
                res.add(temp);
            }
            return res;
        }
        return wrapTx(pailie2(n1,n2,e1-1),n1,e1);
    }

    //nums中位置e的数放入一组数，nums组内有序
    public static List<List<Integer>> wrapTx(List<List<Integer>> pailie,int[] nums, int e) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < pailie.size(); i++) {
            int j=0;
            while (pailie.get(i).get(j)!=nums[e-1])
                j++;
            for (int k = j+1; k <= pailie.get(i).size(); k++) {
                List<Integer> temp = new ArrayList<>();
                for (int l = 0; l < pailie.get(i).size(); l++) {
                    if(l==k)
                        temp.add(nums[e]);
                    temp.add(pailie.get(i).get(l));
                    if(l==pailie.get(i).size()-1&&k==pailie.get(i).size())
                        temp.add(nums[e]);
                }
                res.add(temp);
            }
        }
        return res;
    }


    //一组数的全排列
    public static List<List<Integer>> allPaiLie(int[] nums, int end){
        List<List<Integer>> res = new ArrayList<>();
        if(nums==null|| nums.length==0)
            return null;
        if(nums.length==1||end==0){
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(nums[0]);
            res.add(temp);
            return res;
        }
        if(nums.length==2||end==1){
            ArrayList<Integer> t1 = new ArrayList<>();
            t1.add(nums[0]);t1.add(nums[1]);
            res.add(t1);
            ArrayList<Integer> t2 = new ArrayList<>();
            t2.add(nums[1]);t2.add(nums[0]);
            res.add(t2);
            return res;
        }
        return wrap(allPaiLie(nums,end-1),nums[end]);
    }

    //一个数放入一组数全排
    private static List<List<Integer>> wrap(List<List<Integer>> allPaiLie, int num) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < allPaiLie.size(); i++) {
            for (int j = 0; j <= allPaiLie.get(i).size(); j++) {
                List<Integer> temp = new ArrayList<>();
                for (int k = 0; k < allPaiLie.get(i).size(); k++) {
                    if(j==k)
                        temp.add(num);
                    temp.add(allPaiLie.get(i).get(k));
                    if(k==allPaiLie.get(i).size()-1&&j==allPaiLie.get(i).size())
                        temp.add(num);
                }
                res.add(temp);
            }
        }
        return res;
    }

    public static void main(String[] args) {
//        System.out.println(fun2(4,4));
//        System.out.println(funN(new int[]{2,1,1,1},3));
        List<List<Integer>> list = Arrays.asList(
                Arrays.asList(new Integer[]{1,3,4}),
                Arrays.asList(new Integer[]{3,1,4}),
                Arrays.asList(new Integer[]{3,4,1}));
//        List<List<Integer>> res = wrap(list,3);
//        List<List<Integer>> res = allPaiLie(new int[]{1,2,3,4},3);
//        List<List<Integer>> res = wrapTx(list,new int[]{1,2},1);
//        List<List<Integer>> res = pailie2(new int[]{1,2},new int[]{3,4,5},1);

//        List<List<Integer>> res = wrapN(list,new int[]{5,6});
        List<List<Integer>> res = pailieN(new int[][]{
                {1,2,3},
                {4,5,6},
                {7,8,9}
        },2);
     }
}
