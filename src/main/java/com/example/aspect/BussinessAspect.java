package com.example.aspect;

import com.example.annotation.MyTransactional;
import com.example.entity.Msg;
import com.example.entity.TxManager;
import com.example.exception.MyException;
import com.example.util.LockConditionUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
public class BussinessAspect implements Ordered {
    @Autowired
    private CuratorFramework client;

    @Autowired
    private RestTemplate restTemplate;


    @Around("@annotation(com.example.annotation.MyTransactional)")
    public void around(ProceedingJoinPoint joinPoint) throws Exception {
        Msg msg = new Msg();
        String localId = UUID.randomUUID().toString();
        msg.setLocalId(localId);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyTransactional myTransactional = method.getDeclaredAnnotation(MyTransactional.class);
        int childNum = myTransactional.childNum();
        //header获取groupId
//        msg.setGroupId(TxManager.txGroup.get(Thread.currentThread()));
        msg.setGroupId(TxManager.deliverGroup.get());


        Thread ct = Thread.currentThread();
        String zkLocalId = UUID.randomUUID().toString();

        if (myTransactional.isStart() == true) {
            msg.setIsStartEnd("start");
            TxManager.txGroup.put(Thread.currentThread(), msg.getGroupId());
            CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
            String zkGroupId = msg.getGroupId();
            String pathZkGroupId = "/" + zkGroupId;

            // 创建事务组节点
            new Thread(() -> {
                try {
                    client.create().creatingParentContainersIfNeeded() // 递归创建所需父节点
                            .withMode(CreateMode.PERSISTENT) // 创建类型为永久节点
                            .forPath(pathZkGroupId, "init".getBytes());// zkGroupId作为父节点，状态初始为init
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    TreeCache treeCache = new TreeCache(client, pathZkGroupId);
                    PathChildrenCache pathChildrenCache = new PathChildrenCache(client, pathZkGroupId, true);
                    treeCache.start();
                    pathChildrenCache.start();
                    long sleepTime = 40000l;
                    pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                        @Override
                        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                            ChildData eventData = event.getData();
                            switch (event.getType()) {
                                case CHILD_REMOVED://子节点失联
                                    System.out.println(eventData.getPath() + "节点被删除......");
                                    if(new String(client.getData().forPath(pathZkGroupId)).equals("init"))
                                        client.setData().forPath(pathZkGroupId,"rollBack".getBytes());
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    cyclicBarrier.await();
                    Thread.sleep(1000 * 60 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                deleteNodes(pathZkGroupId);
            }).start();
            cyclicBarrier.await();

        }else{
            try {
                PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/"+msg.getGroupId(), true);
                pathChildrenCache.start();
                pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        ChildData eventData = event.getData();
                        switch (event.getType()) {
                            case CHILD_REMOVED://子节点失联
                                System.out.println(eventData.getPath() + "节点被删除......");
                                if(new String(client.getData().forPath("/"+msg.getGroupId())).equals("init"))
                                    client.setData().forPath("/"+msg.getGroupId(),"rollBack".getBytes());
                                break;
                            default:
                                break;
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (myTransactional.isEnd() == true) {
            msg.setIsStartEnd("end");
            msg.setLocalId(zkLocalId + "end");
        }

        String groupId = msg.getGroupId();
        TxManager.tm.put(groupId, new LockConditionUtil(new ReentrantLock(true), new CountDownLatch(2)));
        TxManager.tm.get(groupId).setClient(client);
        TxManager.tm.get(msg.getGroupId()).setPathGroupId("/" + msg.getGroupId());

        String pathZkGroupId = "/" + msg.getGroupId();
        String pathZkLocalId = pathZkGroupId + "/" + msg.getLocalId();
        TxManager.tm.get(groupId).setPathGroupId(pathZkGroupId);


        // 原本数据库的逻辑
        try {
            joinPoint.proceed();
            msg.setLocalState("commit");

        } catch (Throwable throwable) {
            msg.setLocalState("rollBack");
        } finally {
            new Thread(()->{
                try {
                    if (msg.getLocalState().equals("commit")) {
                        if (client.checkExists().forPath(pathZkGroupId) != null) {        //主节点没挂
                            if (new String(client.getData().forPath(pathZkGroupId)).equals("init")) {//并且是初始状态
                                String localPre = myTransactional.isEnd() == true ? "end" :
                                        (myTransactional.isStart() == true ? "start" : "mid");
                                String curtPath = client.create()
                                        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                                        .forPath(pathZkGroupId + "/ES", localPre.getBytes());//创建临时顺序节点
                                String nextPath = curtPath.substring(0, curtPath.length() - 10) + String.format("%010d", Integer.valueOf(curtPath.substring(curtPath.length() - 10, curtPath.length())) + 1);
                                System.out.println(curtPath + " createTime: " + new Date() + "  next: " + nextPath);
                                long start = System.currentTimeMillis();
                                while (client.checkExists().forPath(nextPath) == null
//                                    && !(hasState(pathZkGroupId,"end") && hasState(pathZkGroupId,"start"))//基本全部节点都到了
                                        && childNum == caculateNum(pathZkGroupId)//节点还没到完
                                            && System.currentTimeMillis() - start < 1000 * 9) {//等下一个节点创建最多等9s
                                }

                                if (client.checkExists().forPath(nextPath) == null) {       //最后一个节点改变主节点值
                                    System.out.println(pathZkGroupId + "endCost: " + (System.currentTimeMillis() - start));
                                    if (new String(client.getData().forPath(pathZkGroupId)).equals("init")
                                            && hasState(pathZkGroupId, "start")
                                            && hasState(pathZkGroupId, "end")
                                            && childNum == caculateNum(pathZkGroupId))
                                        client.setData().forPath(pathZkGroupId, "commit".getBytes());//设置提交
                                    if (new String(client.getData().forPath(pathZkGroupId)).equals("commit")) {
                                        unlock(groupId, "commit");
                                    } else {
                                        client.setData().forPath(pathZkGroupId, "rollBack".getBytes());//设置回滚
                                        unlock(groupId, "rollBack");
                                    }
                                } else {
                                    System.out.println(pathZkGroupId + "pntCost: " + (System.currentTimeMillis() - start));                       //中间节点监听主节点变化
                                    NodeCache nodeCache = new NodeCache(client, pathZkGroupId);
                                    nodeCache.start(true);
                                    nodeCache.getListenable().addListener(() -> {
                                        ChildData eventData = nodeCache.getCurrentData();
                                        String zkGroupState = new String(eventData.getData());
                                        int version = eventData.getStat().getVersion();
                                        if (zkGroupState.equals("commit")) {
                                            System.out.println(groupId + " notifyCommit..." + new Date());
                                            unlock(groupId, "commit");
                                        } else if (zkGroupState.equals("rollBack")) {
                                            System.out.println(groupId + " notifyRoll..." + new Date());
                                            unlock(groupId, "rollBack");
                                        }
                                    });
                                }
                            } else if (new String(client.getData().forPath(pathZkGroupId)).equals("rollBack")) {  //提前被其他系统改成了回滚
                                System.out.println(groupId + " anyRoll..." + new Date());
                                unlock(groupId, "rollBack");
                            }
                        } else { // 主节点挂了回滚
                            System.out.println(groupId + " noGroupRoll..." + new Date());
                            unlock(groupId, "rollBack");
                        }
                    } else if (msg.getLocalState().equals("rollBack")) {//本地回滚，主节点也回滚
                        System.out.println(groupId + " localRoll..." + new Date());
                        unlock(groupId, "rollBack");
                        client.setData().forPath(pathZkGroupId, "rollBack".getBytes());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();

        }
    }

    private void unlock(String groupId, String state) {
        TxManager.tm.get(groupId).setState(state);
        System.out.println(TxManager.tm.get(groupId).getCondition() + state + "end wait time..." + new Date()+groupId);
        TxManager.tm.get(groupId).getLatch().countDown();
        //使用资源后释放资源
        TxManager.tm.remove(groupId);
    }

    @Override
    public int getOrder() {
        return 10000;
    }

    public boolean hasState(String path,String state){
        boolean hasState = false;
        try {
            hasState = client.getChildren().forPath(path).stream().anyMatch(e -> {
                boolean per = false;
                try {
                    per = new String(client.getData().forPath(path + "/" + e)).equals(state);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return per;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasState;
    }

    public int caculateNum(String path){
        try {
            return client.getChildren().forPath(path).size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteNodes(String path){
        try {   //两分钟后删除主节点
            if (client.checkExists().forPath(path) != null
                    && !new String(client.getData().forPath(path)).equals("init")) {
                client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}