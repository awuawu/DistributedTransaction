package com.example.algorithom;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Integer> {

    private Integer start = 0;
    private Integer end = 0;

    public SumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {

        if (end - start < 100) {
            //小于100时直接返回结果
            int sumResult = 0;
            for (int i = start; i <= end; i++) {
                sumResult += i;
            }
            return sumResult;
        } else {
            //大于一百时进行分割
            int middle = (end + start) / 2;
            SumTask leftSum = new SumTask(this.start, middle);
            SumTask rightSum = new SumTask(middle, this.end);
            leftSum.fork();
            rightSum.fork();
            return leftSum.join() + rightSum.join();
        }
    }

    public static void main(String[] args) {
//        SumTask sumTask = new SumTask(1, 999999999);
//
//        long s1 = System.nanoTime();
//        System.out.println("result:" + sumTask.fork().join()+" cost time "+(System.nanoTime()-s1));
//
//        long s2 = System.nanoTime();
//        ForkJoinPool forkJoinPool=new ForkJoinPool(2);
//        forkJoinPool.submit(sumTask);
//        System.out.println("result:" + sumTask.join()+" cost time "+(System.nanoTime()-s2));
        int[] arr = new int[1000001];
        long s1 = System.nanoTime();
        System.out.println(arr[0]);
        System.out.println("cost: "+(System.nanoTime()-s1));

        long s2 = System.nanoTime();
        System.out.println(arr[0]);
        System.out.println("cost: "+(System.nanoTime()-s2));

        long s3 = System.nanoTime();
        System.out.println(arr[1000000]);
        System.out.println("cost: "+(System.nanoTime()-s3));
    }
}
