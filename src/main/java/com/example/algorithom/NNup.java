package com.example.algorithom;

public class NNup {
    public static boolean findMax(int[][] nn,int k,int fromX,int fromY,int toX,int toY){
        if(fromX==toX){
            int res = findMidOne(nn[fromX],k,fromY,toY);
            return res==-2?true:false;
        }
        if(fromY==toY){
            int[] temp = new int[nn.length];
            for (int i = 0; i < nn.length; i++) {
                temp[i] = nn[i][fromY];
            }
            int res = findMidOne(temp,k,fromY,toY);
            return res==-2?true:false;
        }
        int midY = findMidTwo(nn,fromX,k,fromY,toY,true);
        if(midY==-1)
            return false;
        if(midY==-2)
            return true;
        int midX = findMidTwo(nn,fromY,k,fromX,toX,false);
        if(midX==-1)
            return false;
        if(midX==-2)
            return true;
        return findMax(nn,k,fromX+1,fromY+1,midX,midY);
    }

    public static int findMidTwo(int[][] nn,int lo, int k, int from, int to,boolean dir) {
        int mid;
        if(dir)
            mid = findMidOne(nn[lo],k,from,to);
        else{
            int[] temp = new int[nn.length];
            for (int i = 0; i < nn.length; i++) {
                temp[i] = nn[i][lo];
            }
            mid = findMidOne(temp,k,from,to);
        }
        return mid;
    }

    public static int findMidOne(int[] ints, int k, int from, int to) {
        if(k<ints[from])
            return -1;//找不到
        if(k>ints[to])
            return to;
        int i = from;
        while (i<=to){
            if(ints[i]==k)
                return -2;//找到
            if(ints[i]>k)
                break;
            i++;
        }
        return i-1;
    }

    public static void main(String[] args) {
        int[][] nums = new int[][]{
                {1,3,5,7},
                {3,7,8,11},
                {6,9,10,12},
                {13,14,15,16}
        };
//        System.out.println(findMidOne(new int[]{2,4,6},4,0,2));
//        System.out.println(findMidTwo(nums,0,3,0,2,false));
        System.out.println(findMax(nums,-1,0,0,3,3));
    }
}
