package com.example.aspect;

import com.example.annotation.MyTransactional;
import com.example.entity.Msg;
import com.example.entity.TxManager;
import com.example.util.LockConditionUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.ArrayUtils;

@Aspect
@Component
public class BussinessAspect implements Ordered {
    @Autowired
    private CuratorFramework client;


    @Around("@annotation(com.example.annotation.MyTransactional)")
    public void around(ProceedingJoinPoint joinPoint) throws Exception {
        Msg msg = new Msg();
        String localId = UUID.randomUUID().toString();
        msg.setLocalId(localId);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyTransactional myTransactional = method.getDeclaredAnnotation(MyTransactional.class);
        //1.这里获取到所有的参数值的数组
        Object[] args = joinPoint.getArgs();
        //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();
        int timeStampIndex = ArrayUtils.indexOf(parameterNames, "groupId");
        if (timeStampIndex != -1) { //非事务发起者设置groupId
            String groupId = (String) args[timeStampIndex];
            msg.setGroupId(groupId);
            TxManager.txGroup.put(Thread.currentThread(), groupId);
        }

        Thread ct = Thread.currentThread();
        String zkLocalId = UUID.randomUUID().toString();

        if (myTransactional.isStart() == true) {

            //模拟netty方式
            msg.setIsStartEnd("start");
            String groupId = UUID.randomUUID().toString();
            msg.setGroupId(groupId);
            TxManager.txGroup.put(Thread.currentThread(), groupId);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
            //zookeeper方式
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
                                case CONNECTION_LOST:
                                    System.out.println(eventData.getPath() + "节点断开连接......");
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
                try {   //两分钟后删除主节点
                    if (client.checkExists().forPath(pathZkGroupId) != null
                            && !new String(client.getData().forPath(pathZkGroupId)).equals("init")) {
                        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathZkGroupId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            cyclicBarrier.await();

            // 监听子节点事件
            /*try {
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
                            case CHILD_ADDED:
                                String zkLocalId = new String(eventData.getData());
                                System.out.println("/" + zkGroupId + "节点添加" + eventData.getPath() + "\t添加数据为：" + zkLocalId);
                                if (eventData.getPath().endsWith("end")) {
                                    Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(pathZkGroupId);
                                    long start = System.currentTimeMillis();
                                    while (isAnyInit(pathZkGroupId)//有子节点处于刚创建状态
                                            && (System.currentTimeMillis()-start < sleepTime));//等待时间未超时
                                    System.out.println("castTime: "+(System.currentTimeMillis()-start));
                                    boolean isCommit = isAllCommit(pathZkGroupId);
                                    System.out.println("groupState: "+isCommit);
                                    System.out.println("groupData: "+new String(client.getData().forPath(pathZkGroupId)));
                                    if (isCommit && getData(pathZkGroupId).equals("init")) {
                                        System.out.println("setCommit...");
                                        setData(pathZkGroupId,"commit");
                                    } else {
                                        System.out.println("setRollBack...");
                                        setData(pathZkGroupId,"rollBack");
                                    }
                                }
                                break;
                            case CONNECTION_LOST:
                                System.out.println(eventData.getPath() + "节点被删除");
                                if(getData(pathZkGroupId)!=null&&getData(pathZkGroupId).equals("init")) {
                                    setData(pathZkGroupId,"rollBack");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }*/
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

        /*try {
            if(client.checkExists().forPath(pathZkGroupId)!=null) {
                client.create() // 非递归创建所需父节点，必须要有事务组
                        .withMode(CreateMode.EPHEMERAL) // 创建类型为临时节点
                        .forPath(pathZkLocalId, "init".getBytes()); // zkGroupId作为父节点，状态初始为init
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //监听父节点，看父节点状态变化
        /*try {
            NodeCache nodeCache = new NodeCache(client, pathZkGroupId);
            nodeCache.start(true);
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    ChildData eventData = nodeCache.getCurrentData();
                    if(eventData == null){
                        System.out.println("groupId removed: "+pathZkGroupId);
                        TxManager.tm.get(groupId).setState("rollBack");
                        System.out.println("unlock...");
                        TxManager.tm.get(groupId).getLock().lock();
                        TxManager.tm.get(groupId).getCondition().signalAll();
                        TxManager.tm.get(groupId).getLock().unlock();
                    }else{
                        String zkGroupState = new String(eventData.getData());
                        int version = eventData.getStat().getVersion();
                        System.out.println("changedTimes: " +zkGroupState+version);
                        if (zkGroupState.equals("commit") && version == 1) {
                            TxManager.tm.get(groupId).setState("commit");
                            System.out.println("commitunlock...");
                            System.out.println("unlockThread: "+ct.getId());
                            TxManager.tm.get(groupId).getLock().lock();
                            System.out.println(TxManager.tm.get(groupId).getCondition()+"end wait time..."+new Date());
                            TxManager.tm.get(groupId).getCondition().signalAll();
                            TxManager.tm.get(groupId).getLock().unlock();
                        }else if(zkGroupState.equals("rollBack") && version == 1){
                            TxManager.tm.get(groupId).setState("rollBack");
                            System.out.println("rollBackunlock...");
                            System.out.println("unlockThread: "+ct.getId());
                            TxManager.tm.get(groupId).getLock().lock();
                            System.out.println(TxManager.tm.get(groupId).getCondition()+"end wait time..."+new Date());
                            TxManager.tm.get(groupId).getCondition().signalAll();
                            TxManager.tm.get(groupId).getLock().unlock();
                        }else if(version==eventData.getStat().getNumChildren()+1&&myTransactional.isStart()==true){
                            try {
                                client.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathZkGroupId);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // 原本数据库的逻辑
        try {
            joinPoint.proceed();
            msg.setLocalState("commit");
//            long startc = System.currentTimeMillis();
//            while(client.checkExists().forPath(pathZkLocalId)!=null&&(System.currentTimeMillis()-startc<100));
//            client.setData().forPath(pathZkLocalId,msg.getLocalState().getBytes());

        } catch (Throwable throwable) {
            msg.setLocalState("rollBack");
//            try {
//                long startr = System.currentTimeMillis();
//                while(client.checkExists().forPath(pathZkLocalId)!=null&&(System.currentTimeMillis()-startr<100));
//                client.setData().forPath(pathZkLocalId,msg.getLocalState().getBytes());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            throwable.printStackTrace();
        } finally {
            // 非事务开启者获取事务组ID

//            while (TxManager.tm.get(ct).getState()==null&&(System.currentTimeMillis()-start)<60000l);
//            TxManager.tm.get(ct).getLock().lock();
//            TxManager.tm.get(ct).getCondition().signalAll();
//            TxManager.tm.get(ct).getLock().unlock();
            // 模拟netty方式
            /*System.out.println("wait res...");
            Msg res = restTemplate.postForObject("http://localhost:8083/manager", msg, Msg.class);
            System.out.println("resMessage: "+res);
            System.out.println("bussinessThread: "+Thread.currentThread());
            TxManager.tm.get(Thread.currentThread()).setState(res.getGroupState());
            TxManager.tm.get(Thread.currentThread()).getLock().lock();
            TxManager.tm.get(Thread.currentThread()).getCondition().signalAll();
            TxManager.tm.get(Thread.currentThread()).getLock().unlock();*/

            if (msg.getLocalState().equals("commit")) {
                if (client.checkExists().forPath(pathZkGroupId) != null) {        //主节点没挂
                    if (new String(client.getData().forPath(pathZkGroupId)).equals("init")) {//并且是初始状态
                        String curtPath = client.create()
                                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                                .forPath(pathZkGroupId + "/E", "commit".getBytes());//创建临时顺序节点
                        String nextPath = curtPath.substring(0, curtPath.length() - 10) + String.format("%010d", Integer.valueOf(curtPath.substring(curtPath.length() - 10, curtPath.length())) + 1);
                        System.out.println(curtPath+" createTime: "+System.currentTimeMillis());
                        long start = System.currentTimeMillis();
                        while (client.checkExists().forPath(nextPath) == null && System.currentTimeMillis() - start < 1000 * 9) {//等下一个节点创建最多等9s
                            Thread.sleep((long) (Math.random() * 100));
                        }

                        if (client.checkExists().forPath(nextPath) == null) {       //最后一个节点改变主节点值
                            System.out.println("endCost: " + (System.currentTimeMillis() - start));
                            if(new String(client.getData().forPath(pathZkGroupId)).equals("init"))
                                client.setData().forPath(pathZkGroupId, "commit".getBytes());//设置提交
                            if(new String(client.getData().forPath(pathZkGroupId)).equals("commit"))
                                unlock(groupId, "commit");
                            else
                                unlock(groupId,"rollBack");
                        } else {
                            System.out.println("pntCost: " + (System.currentTimeMillis() - start));                       //中间节点监听主节点变化
                            NodeCache nodeCache = new NodeCache(client, pathZkGroupId);
                            nodeCache.start(true);
                            nodeCache.getListenable().addListener(() -> {
                                ChildData eventData = nodeCache.getCurrentData();
                                String zkGroupState = new String(eventData.getData());
                                int version = eventData.getStat().getVersion();
                                if (zkGroupState.equals("commit")) {
                                    System.out.println(groupId+ " notifyCommit..."+new Date());
                                    unlock(groupId, "commit");
                                } else if (zkGroupState.equals("rollBack")) {
                                    System.out.println(groupId+ " notifyRoll..."+new Date());
                                    unlock(groupId, "rollBack");
                                }
                            });
                        }
                    } else if (new String(client.getData().forPath(pathZkGroupId)).equals("rollBack")) {  //提前被其他系统改成了回滚
                        System.out.println(groupId+" anyRoll..."+new Date());
                        unlock(groupId, "rollBack");
                    }
                } else { // 主节点挂了回滚
                    System.out.println(groupId+" noGroupRoll..."+new Date());
                    unlock(groupId, "rollBack");
                }
            }
            if (msg.getLocalState().equals("rollBack")) {//本地回滚，主节点也回滚
                System.out.println(groupId+" localRoll..."+new Date());
                unlock(groupId, "rollBack");
                client.setData().forPath(pathZkGroupId, "rollBack".getBytes());
            }
        }
    }

    private void unlock(String groupId, String state) {
        TxManager.tm.get(groupId).setState(state);
        /*TxManager.tm.get(groupId).getLock().lock();
        System.out.println(TxManager.tm.get(groupId).getCondition()+ state + "end wait time..." + new Date());
        TxManager.tm.get(groupId).getCondition().signalAll();
        TxManager.tm.get(groupId).getLock().unlock();*/
        System.out.println(TxManager.tm.get(groupId).getCondition() + state + "end wait time..." + new Date()+groupId);
        TxManager.tm.get(groupId).getLatch().countDown();
    }

    @Override
    public int getOrder() {
        return 10000;
    }

}