package com.example.algorithom;

import java.util.*;

public class Main {
    static Stack<Integer[]> stack = new Stack<>();
    public static void main(String[] args) {

        //Scanner in = new Scanner(System.in);
        //int a = in.nextInt();
        //System.out.println(a);
//        int[][] matrix = new int[][]{
////                 {1,-1, 1, 1, 1},
////                 {1, 1,-1, 1, 1},
//                 {1, 1, 1,-1, 1},
//                 {1,-1,-1, 1, 1},
//                 {1, 1, 1, 1, 1}
//        };
        int[][] matrix = new int[][]{
                {-1,-1, 1, 1},
                { 1, 1, 1, 1}
        };
        int[][] memo = new int[matrix.length][matrix[0].length];
        System.out.println(findMinWay(matrix,1,0,1,3,new HashSet<>(),memo));
    }
    public static int findMinWay(int[][] matrix, int x1, int y1, int x2, int y2, Set<String> location,int[][] memo) {
        //墙返回
        if (matrix[x1][y1] == -1 || matrix[x2][y2] == -1)
            return 10000000;
        //间隔为1
        if ((Math.abs(x1 - x2) == 0 && Math.abs(y1 - y2) == 1) ||
                (Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 0)) {
            return 1;
        }
        if(memo[x2][y2]!=0)
            return memo[x2][y2];
        //死路直接返回
//        if (x2-1>=0&&matrix[x2 - 1][y2] == -1
//                &&y2-1>=0&& matrix[x2][y2 - 1] == -1
//                &&x2+1<matrix.length&& matrix[x2 + 1][y2] == -1
//                &&y2+1<matrix[0].length&& matrix[x2][y2 + 1] == -1)
//            return 10000000;

        int left = 10000000;
        int up = 10000000;
        int right = 10000000;
        int down = 10000000;
        location.add(x2+"."+y2);
        //从上边来
        if (x2 - 1 >= 0 && matrix[x2 - 1][y2] == 1
                &&memo[x2][y2]==0) {
            left = findMinWay(matrix, x1, y1, x2 - 1, y2, location,memo) + 1;
            memo[x2][y2] = left;
        }
        //从左边来
        if (y2 - 1 >= 0 && matrix[x2][y2 - 1] == 1
                &&memo[x2][y2]==0) {
            up = findMinWay(matrix, x1, y1, x2, y2 - 1, location,memo) + 1;
            memo[x2][y2] = up;
        }
        //从下边来
        if (x2 + 1 < matrix.length && matrix[x2 + 1][y2] == 1
                &&memo[x2][y2]==0) {
            right = findMinWay(matrix, x1, y1, x2 + 1, y2, location,memo) + 1;
            memo[x2][y2] = right;
        }
        //从右边来
        if (y2 + 1 < matrix[0].length && matrix[x2][y2 + 1] == 1
                &&memo[x2][y2]==0) {
            down = findMinWay(matrix, x1, y1, x2, y2 + 1, location,memo) + 1;
            memo[x2][y2] = down;

        }
        return Math.min(left, Math.min(up, Math.min(right, down)));

    }
}