package com.example.algorithom;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiDi {
    public static Lock lockA = new ReentrantLock();
    public static Lock lockB = new ReentrantLock();
    public static Lock lockC = new ReentrantLock();
    public static Condition conA = lockA.newCondition();
    public static Condition conB = lockB.newCondition();
    public static Condition conC = lockC.newCondition();


    public static void main(String[] args) {
        new Thread(()->{
            int k=10;
            while (k>0) {
                try {
                    lockA.lock();
                    conA.await();
                    lockA.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("A");
                lockB.lock();
                conB.signalAll();
                lockB.unlock();
                k--;
            }
        }).start();
        new Thread(()->{
            int k=10;
            while (k>0) {
                try {
                    lockB.lock();
                    conB.await();
                    lockB.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("B");
                lockC.lock();
                conC.signalAll();
                lockC.unlock();
                k--;
            }
        }).start();
        new Thread(()->{
            int k=10;
            while (k>0) {
                try {
                    lockC.lock();
                    conC.await();
                    lockC.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("C");
                lockA.lock();
                conA.signalAll();
                lockA.unlock();
                k--;
            }
        }).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lockA.lock();
        conA.signalAll();
        lockA.unlock();
    }
}
