package com.example.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configer {
    @Bean
    public CuratorFramework curatorFramework(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                50000, 50000, retryPolicy);
        client.start();
        NodeCache nodeCache = new NodeCache(client,"/nodeA");
        nodeCache.getListenable().addListener(()->{
            ChildData eventData = nodeCache.getCurrentData();
            if(eventData==null){
                System.out.println("node removed...");
            }else{
                System.out.println(Thread.currentThread()+"node changed: "+new String(eventData.getData()));
            }
        });
        try {
            nodeCache.start(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
