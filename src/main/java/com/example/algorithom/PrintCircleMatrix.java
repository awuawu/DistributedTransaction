package com.example.algorithom;

/*
*1  2  3  4  5
*16 17 18 19 6
*15 24 25 20 7
*14 23 22 21 8
*13 12 11 10 9*/

import java.util.concurrent.CountDownLatch;

public class PrintCircleMatrix {
    public static int[][] matrix = new int[5][5];
    static class Candi{
        int i;
        int j;
        int d;
        Candi(int i,int j,int d){
            this.i = i;this.j = j;this.d = d;
        }
    }
    public static Candi[] writeMatrix(Candi start,Candi end){
        Candi[] next = new Candi[2];
        if(start.i==end.i){
            if(start.j<end.j){
                //填充
                for (int i = start.j; i <= end.j ; i++)
                    matrix[start.i][i] = start.d++;

                //找下一段
                next[0] = new Candi(end.i+1, end.j, end.d+1);
                int len = 0;
                for (int i = end.i+1; i < matrix.length&&matrix[i][end.j]!=1; i++)
                    len++;
                next[1] = new Candi(end.i + len, end.j,end.d + len);
            }
            if(start.j>end.j){
                for (int i = start.j; i >= end.j ; i--)
                    matrix[start.i][i] = start.d++;

                //找下一段
                next[0] = new Candi(end.i-1, end.j, end.d+1);
                int len = 0;
                for (int i = end.i-1; i >= 0&&matrix[i][end.j]!=1; i--)
                    len++;
                next[1] = new Candi(end.i - len, end.j,end.d + len);
            }
        }
        if(start.j==end.j){
            if(start.i<end.i){
                for (int i = start.i; i <= end.i ; i++)
                    matrix[i][start.j] = start.d++;

                //找下一段
                next[0] = new Candi(end.i, end.j-1, end.d+1);
                int len = 0;
                for (int i = end.j-1; i >=0&&matrix[end.i][i]!=1; i--)
                    len++;
                next[1] = new Candi(end.i, end.j - len,end.d + len);
            }
            if(start.i>end.i){
                for (int i = start.i; i >= end.i ; i--)
                    matrix[i][start.j] = start.d++;

                //找下一段
                next[0] = new Candi(end.i, end.j+1, end.d+1);
                int len = 0;
                for (int i = end.j+1; i < matrix.length&&matrix[end.i][i]!=1; i++)
                    len++;
                next[1] = new Candi(end.i, end.j + len,end.d + len);
            }
        }
        if(next[0].i==next[1].i&&next[0].j==next[1].j)
            return null;
        else
            return writeMatrix(next[0],next[1]);
    }


    public static void main(String[] args) {
        writeMatrix(new Candi(0,0,1),new Candi(0,4,5));
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.println(matrix[i][j]);
            }
            System.out.println();
        }
    }

}
