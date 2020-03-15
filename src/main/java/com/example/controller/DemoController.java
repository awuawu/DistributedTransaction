package com.example.controller;

import com.example.annotation.MyTransactional;
import com.example.dao.DemoMapper;
import com.example.entity.TxManager;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.example.service.ZookeeperService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class DemoController {

    public static Map<String,Integer> map = new ConcurrentHashMap<>();

    @Autowired
    private DemoMapper demoMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CuratorFramework zkClient;

    @Autowired
    private ZookeeperService ZookeeperService;

    @RequestMapping("/creat")
    public void creat(){
        demoMapper.createTable();
    }

    @RequestMapping("/insert")
    @Transactional
    public void insertData(String title,String author){
        demoMapper.insertData(title,author);
    }

    @RequestMapping("/hello")
    public String helloworld(String path,String data){
        return ZookeeperService.createSequentialEphemeralNode(path, data);
    }

    @RequestMapping("/")
    public String fssd(){
        return "Hello World 呵呵";
    }

//    @Transactional
    @MyTransactional(isStart = true)
    @RequestMapping("/call")
    public String call(String title, String author, Integer num1, Integer num2, String groupId){
        demoMapper.insertData("t3","a3");
        Thread ct = Thread.currentThread();
        new Thread(()->{ String res = restTemplate.getForObject("http://localhost:8082/call?groupId={1}&title={2}&author={3}&num2={4}",String.class, TxManager.txGroup.get(ct),title,author,num2);}).start();
        System.out.println("caculate: "+10/num1);
        return "sucessfully!";
    }




    @RequestMapping("/zkCreat")
    public void zkCreat(String path){
        try {
            zkClient.create().creatingParentContainersIfNeeded() // 递归创建所需父节点
                    .withMode(CreateMode.PERSISTENT) // 创建类型为持久节点
                    .forPath("/"+path, "init".getBytes()); // 目录及内容
            //1. 创建一个NodeCache
            NodeCache nodeCache = new NodeCache(zkClient, "/"+path);

            //2. 添加节点监听器
            nodeCache.getListenable().addListener(() -> {
                ChildData childData = nodeCache.getCurrentData();
                if(childData != null){
                    System.out.println("Pathth: " + childData.getPath());
                    System.out.println("Stat:" + childData.getStat());
                    System.out.println("Data: "+ new String(childData.getData()));
                }
            });

            //3. 启动监听器
            nodeCache.start();

            TreeCache treeCache = new TreeCache(zkClient, "/"+path);
            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, "/"+path, true);
            treeCache.start();
            pathChildrenCache.start();
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    ChildData eventData = event.getData();
                    switch (event.getType()) {
                        case CONNECTION_LOST:
                            System.out.println(eventData.getPath() + "节点断开连接......");
                            if(new String(client.getData().forPath("/"+path)).equals("init"))
                                client.setData().forPath("/"+path,"rollBack".getBytes());
                            break;
                        case CHILD_REMOVED:
                            System.out.println(eventData.getPath() + "节点被删除......"+new Date());
                            if(new String(client.getData().forPath("/"+path)).equals("init"))
                                client.setData().forPath("/"+path,"rollBack".getBytes());
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


    @RequestMapping("/getData")
    public void getData(String path){
        try {
            String s = new String(zkClient.getData().forPath("/"+path));
            System.out.println("getData: " + s);
            String st = new String(zkClient.getData().storingStatIn(new Stat(
             1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1
            )).forPath("/"+path));
            System.out.println("getStat: " + st);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/deleteNode")
    public void deleteNode(String path){
        try {
            List<String> strings = zkClient.getChildren().forPath("/");
            for (int i = 0; i < strings.size(); i++) {
                if (!strings.get(i).equals("zookeeper"))
                zkClient.delete().deletingChildrenIfNeeded().forPath("/"+strings.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 注册监听
     * TreeCache: 可以将指定的路径节点作为根节点（祖先节点），对其所有的子节点操作进行监听，
     * 呈现树形目录的监听，可以设置监听深度，最大监听深度为 int 类型的最大值。
     */
    @RequestMapping("/zkWatch")
    public void zkWatch(String pathZkGroupId,int num) throws Exception {
        CuratorFramework client = zkClient;
        CountDownLatch latch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            new Thread(()->{
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                createAndWatch(pathZkGroupId);
            }).start();
            latch.countDown();
        }

    }

    public void createAndWatch(String pathZkGroupId){
        CuratorFramework client = zkClient;
        String curtPath = null;//创建临时顺序节点
        try {
            curtPath = client.create()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath("/"+pathZkGroupId + "/E", "commit".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String nextPath = curtPath.substring(0,curtPath.length()-10) + String.format("%010d",Integer.valueOf(curtPath.substring(curtPath.length()-10,curtPath.length()))+1);
        System.out.println(curtPath+" createTime: "+System.currentTimeMillis());
        try {
            long start = System.currentTimeMillis();
            while(client.checkExists().forPath(nextPath)==null&&System.currentTimeMillis()-start<60000){
                Thread.sleep((long) (Math.random()*100));
            }
            System.out.println("cost: "+(System.currentTimeMillis()-start));
            if(client.checkExists().forPath(nextPath)==null){
                System.out.println("end...");
            }else{
                System.out.println("pnt...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}