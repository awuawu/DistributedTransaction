package com.example.algorithom;

import java.util.concurrent.CountDownLatch;

public class KuLe {
    public static int[][] handle(int[][] mn,int[][] xy){
        int[][] res = new int[mn.length][mn[0].length];
        for (int i = 0; i < mn.length; i++) {
            for (int j = 0; j < mn[0].length; j++) {
                res[i][j]=caculate(mn,xy,i,j);
            }
        }
        return res;
    }

    private static int caculate(int[][] mn, int[][] xy, int i, int j) {
        int xylen = xy.length/2;
        int xykua = xy[0].length/2;
        int mnleft = (i - xylen)>=0?(i - xylen):0;
        int mnup = (j - xykua)>=0?(j - xykua):0;
        int mnright = (i + xylen)<mn.length?(i + xylen):mn.length-1;
        int mndown = (j + xykua)<mn[0].length?(j + xykua):mn[0].length-1;

        int xyleft = (i - xylen)>=0?0:(xylen - i);
        int xyup = (j - xykua)>=0?0:(xykua - j);
        int xyright = (i + xylen)<mn.length?xy.length-1:mn.length-1-i+xylen;
        int xydown = (j + xykua)<mn[0].length?xy[0].length-1:mn[0].length-1-j+xykua;

        int res = 1;
        for (int k = mnleft,c = xyleft; k <= mnright; k++,c++) {
            for (int l = mnup,v = xyup; l <= mndown; l++,v++) {
                if(mn[k][l]!=xy[c][v]) {
                    return 0;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] mn = new int[][]{
                {1,1,1,1},
                {0,1,1,1},
                {1,1,1,1}
        };
        int[][] xy = new int[][]{
//                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,0,1,1},
                {1,1,1,1,1},
//                {1,1,1,1,1}
        };
        int[][] res = handle(mn,xy);
        System.out.println("csduch");
        System.out.println(1!=1);
    }
}
