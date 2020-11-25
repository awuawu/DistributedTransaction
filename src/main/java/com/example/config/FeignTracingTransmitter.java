package com.example.config;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//feign调用时传输groupId
@ConditionalOnClass(Feign.class)
@Component
@Order
public class FeignTracingTransmitter implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Tracings.transmit(requestTemplate::header);
    }
}
