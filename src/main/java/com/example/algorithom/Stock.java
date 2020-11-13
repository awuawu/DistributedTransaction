package com.example.algorithom;

import java.util.HashMap;

public class Stock {
    public static HashMap<String, Integer> stepValue(HashMap<String, Integer> curValue, int newStock){
        if(curValue.isEmpty()){
            curValue.put("firDid", newStock);
        }else if(curValue.size()==1){
            if(newStock<=curValue.get("firDid"))
                curValue.put("firDid", newStock);
            else
                curValue.put("firTop", newStock);
        }
        else if(curValue.size()==2){
            if(newStock<curValue.get("firTop"))
                curValue.put("secDid", newStock);
            else
                curValue.put("firTop", newStock);

        }
        else if(curValue.size()==3){
            if(newStock<=curValue.get("secDid"))
                curValue.put("secDid", newStock);
            else
                curValue.put("secTop", newStock);

        }
        else if(curValue.size()==4){
            if(newStock>curValue.get("secTop"))
                curValue.put("secTop", newStock);
            else if(newStock<curValue.get("secTop"))
                curValue.put("potential", newStock);

        }
        else if(curValue.size()==5){
            if(newStock<=curValue.get("potential"))
                curValue.put("potential", newStock);
            else{
                int[] next = findMaxStep(new int[]{curValue.get("firDid"),curValue.get("firTop"),curValue.get("secDid"),curValue.get("secTop"),curValue.get("potential"),newStock});
                if(next.length==2){
                    curValue.put("firDid",next[0]);
                    curValue.put("firTop",next[1]);
                    curValue.remove("secDid");
                    curValue.remove("secTop");
                    curValue.remove("potential");
                }else if(next.length==4){
                    curValue.put("firDid",next[0]);
                    curValue.put("firTop",next[1]);
                    curValue.put("secDid",next[2]);
                    curValue.put("secTop",next[3]);
                    curValue.remove("potential");
                }
            }

        }
        return curValue;
    }

    public static void printMap(HashMap<String, Integer> map){
        if (map!=null&&!map.isEmpty()){
            for(String key:map.keySet()){
                System.out.println(key+" : "+map.get(key));
            }
        }
    }

    public static int maxProfit(int[] prices) {
        if(prices==null||prices.length<2)
            return 0;
        HashMap<String, Integer> curr = stepValue(new HashMap<String, Integer>(), prices[0]);
        HashMap<String, Integer> next = stepValue(curr, prices[1]);
        for (int i = 2; i < prices.length; i++) {
            curr = next;
            next = stepValue(curr, prices[i]);
        }
        if(next.size()<2)
            return 0;
        else if(next.size()>=2&&next.size()<4){
            return next.get("firTop")-next.get("firDid");
        }
        else if(next.size()>=4){
            return (next.get("firTop")-next.get("firDid"))+(next.get("secTop")-next.get("secDid"));
        }
        return 0;
    }

    public static int[] findMaxStep(int[] candi){//1,4,2,7,2,4
        int stepOne = candi[1]-candi[0];
        int stepTwo = candi[3]-candi[2];
        int stepThr = candi[5]-candi[4];
        int min = Math.min(candi[0],Math.min(candi[2],candi[4]));//1
        int onlyOne = candi[5]-min;//3
        int tail = stepThr + Math.max(candi[3] - candi[0], Math.max(stepOne, stepTwo));//8
        int head = (candi[5] - candi[2]) + stepOne;//5
        if(onlyOne>=tail&&onlyOne>=head&&onlyOne>=(stepOne+stepTwo)){
            return new int[]{min,candi[5]};
        } else if(head>=(stepOne+stepTwo)||tail>=(stepOne+stepTwo)) {
            if (head > tail) {
                return new int[]{candi[0], candi[1], candi[2], candi[5]};
            } else {
                if (candi[3] - candi[0] > Math.max(stepOne, stepTwo))
                    return new int[]{candi[0], candi[3], candi[4], candi[5]};
                else if (stepOne > stepTwo)
                    return new int[]{candi[0], candi[1], candi[4], candi[5]};
                else
                    return new int[]{candi[2], candi[3], candi[4], candi[5]};
            }
        }
        return new int[]{};
    }

    //status为0表示不动状态，1表示买入状态
    public static int dfs(int[] prices,int index,int status,int k) {//index表示第几天；status表示状态买，卖，不动；k表示交易次数
        //递归终止条件，数组执行到头了，或者交易了两次了
        if(index==prices.length || k==2) {
            return 0;
        }
        //定义三个变量，分别记录[不动]、[买]、[卖]
        int a=0,b=0,c=0;
        //保持不动
        a = dfs(prices,index+1,status,k);//不动收益不变
        if(status==1) {
            //递归处理卖的情况，卖多少收益多少，这里需要将k+1，表示执行了一次交易
            b = dfs(prices,index+1,0,k+1)+prices[index];
        }
        else {
            //递归处理买的情况，买多少损失多少
            c = dfs(prices,index+1,1,k)-prices[index];//1,a=0;c=-p0+p1
        }
        //最终结果就是三个变量中的最大值
        return Math.max(Math.max(a,b),c);
    }

    public static int maxProfitDD(int[] prices) {
        int buy1 = Integer.MAX_VALUE;//只买进一次产生的亏损
        int buy2 = Integer.MAX_VALUE;//只买进二次产生的亏损
        int pro1 = 0;//只卖出一次产生的收益
        int pro2 = 0;//只卖出二次产生的收益
        for(int price : prices) {
            buy1 = Math.min(buy1, price);
            pro1 = Math.max(pro1, price - buy1);
            buy2 = Math.min(buy2, price - pro1);
            pro2 = Math.max(pro2, price - buy2);
        }
        return pro2;
    }



    public static void main(String[] args) {
//        int[] nums = new int[]{3,3,5,0,0,3,1,4};
//        int[] nums = new int[]{1,2,3,4,5};
//        int[] nums = new int[]{7,6,4,3,1};
//        int[] nums = new int[]{14,9,10,12,4,8,1,16};
        int[] nums = new int[]{1,2,4,2,5,7,2,4,9,0};
//        int[] nums = new int[]{8,3,6,2,8,8,8,4,2,0,7,2,9,4,9};
//        int[] nums = new int[]{1,1,2,2,1,1,3,3,2,4,6,3,10,5,3};
        System.out.println("stock: "+maxProfit(nums));
        System.out.println("stock: "+dfs(nums,0,0,0));
        System.out.println("stock: "+maxProfitDD(nums));
        System.out.println("********************");
        HashMap<String, Integer> curr = stepValue(new HashMap<String, Integer>(), nums[0]);
        HashMap<String, Integer> next = stepValue(curr, nums[1]);
        for (int i = 2; i < nums.length; i++) {
            curr = next;
            next = stepValue(curr, nums[i]);
        }
        printMap(next);
        System.out.println("*******************");
        HashMap<String, Integer> stringIntegerHashMap = stepValue(new HashMap<String, Integer>(), 90);
        printMap(stringIntegerHashMap);
        System.out.println("----------------------");

        stringIntegerHashMap = stepValue(stringIntegerHashMap,100);
        printMap(stringIntegerHashMap);
        System.out.println("----------------------");

        stringIntegerHashMap = stepValue(stringIntegerHashMap,80);
        printMap(stringIntegerHashMap);
        System.out.println("----------------------");

        stringIntegerHashMap = stepValue(stringIntegerHashMap,100);
        printMap(stringIntegerHashMap);
        System.out.println("----------------------");

        stringIntegerHashMap = stepValue(stringIntegerHashMap,1);
        printMap(stringIntegerHashMap);
        System.out.println("----------------------");

        stringIntegerHashMap = stepValue(stringIntegerHashMap,26);
        printMap(stringIntegerHashMap);
        System.out.println("----------------------");
    }
}
