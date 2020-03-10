package com.example.util;

import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionUtil {
    private Lock lock ;
    private Condition condition;
    private CountDownLatch latch;
    private volatile String state;
    private CuratorFramework client;
    private volatile String pathGroupId;

    public LockConditionUtil(Lock lock,CountDownLatch latch){
        this.lock = lock;
        this.condition = this.lock.newCondition();
        this.latch = latch;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }

    public String getPathGroupId() {
        return pathGroupId;
    }

    public void setPathGroupId(String pathGroupId) {
        this.pathGroupId = pathGroupId;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}
