package com.example.config;

import com.example.entity.TxManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

//拦截器收到groupId时放置groupId,没收到生成
@ConditionalOnClass(HandlerInterceptor.class)
@Component
public class SpringTracingApplier implements HandlerInterceptor, WebMvcConfigurer {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String groupId = request.getHeader("groupId");//从请求中获取groupId
        System.out.println("请求： "+request);
        if(groupId != null){
            System.out.println("下游从上游得到groupId: "+groupId);
        }
        if(groupId == null){//事务发起者新生成groupId
            groupId = UUID.randomUUID().toString();
            System.out.println("new transaction: "+groupId);
        }

//        TxManager.txGroup.put(Thread.currentThread(), groupId);//保存groupId
        TxManager.deliverGroup.set(groupId);
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }
}