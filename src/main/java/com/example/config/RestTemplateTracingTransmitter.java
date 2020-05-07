package com.example.config;

import com.example.entity.TxManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Date: 19-1-28 下午4:35
 *
 * @author ujued
 */
@ConditionalOnClass(RestTemplate.class)
@Component
@Order
public class RestTemplateTracingTransmitter implements ClientHttpRequestInterceptor {

    @Autowired
    public RestTemplateTracingTransmitter(@Autowired(required = false) List<RestTemplate> restTemplates) {
        if (Objects.nonNull(restTemplates)) {
            restTemplates.forEach(restTemplate -> {
                List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
                interceptors.add(interceptors.size(), RestTemplateTracingTransmitter.this);
            });
        }
    }

//    @Override
    @NonNull
    public ClientHttpResponse intercept(
            @NonNull HttpRequest httpRequest, @NonNull byte[] bytes,
            @NonNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String groupId = TxManager.txGroup.get(Thread.currentThread());
        if(groupId != null){
            httpRequest.getHeaders().add("groupId", groupId);
        }
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

    public static void main(String[] agrs){
        ThreadLocal<String> local = new ThreadLocal<>();
        local.set("hhaha");
        new Thread(()->{
            System.out.println("sun: "+local.get());}).start();
        System.out.println("father: "+local.get());
    }
}
