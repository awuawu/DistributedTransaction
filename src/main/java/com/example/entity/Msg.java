package com.example.entity;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Msg {
    private String groupId;
    private String localId;
    private String groupState;
    private String localState;
    private String isStartEnd;
    public static volatile int num;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getGroupState() {
        return groupState;
    }

    public void setGroupState(String groupState) {
        this.groupState = groupState;
    }

    public String getLocalState() {
        return localState;
    }

    public void setLocalState(String localState) {
        this.localState = localState;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "groupId='" + groupId + '\'' +
                ", localId='" + localId + '\'' +
                ", groupState='" + groupState + '\'' +
                ", localState='" + localState + '\'' +
                ", isStartEnd='" + isStartEnd + '\'' +
                '}';
    }

    public String getIsStartEnd() {
        return isStartEnd;
    }

    public void setIsStartEnd(String isStartEnd) {
        this.isStartEnd = isStartEnd;
    }


    /**
     * Tracing信息设置器
     */
    public interface TracingSetter {
        /**
         * 设置tracing属性
         *
         * @param key   key
         * @param value value
         */
        void set(String key, String value);
    }

    public static void main(String[] agrs){
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        System.out.println(String.format("%010d",12));
        String s = "123456789";
        System.out.println( s.substring(0,s.length()-5) + (Integer.valueOf(s.substring(s.length()-5,s.length()))+1));
        new Thread(()->{
            threadLocal.set("1...");
            System.out.println("get: "+threadLocal.get());
        }).start();
        new Thread(()->{
            threadLocal.set("2...");
            System.out.println("get: "+threadLocal.get());
        }).start();
        threadLocal.set("main...");
        System.out.println("get: "+threadLocal.get());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] num=new int[]{2,5,7,9,11,12};
        System.out.println(isContain(num,0,5,23));
    }

    public static boolean isContain(int[] num,int s,int e,int r){
        if(s==e){
            return num[s]==r;
        }
        return isContain(num,s+1,e,r-num[s])||isContain(num,s+1,e,r);
    }


}
