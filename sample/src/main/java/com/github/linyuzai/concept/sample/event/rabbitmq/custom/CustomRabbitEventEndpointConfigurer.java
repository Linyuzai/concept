package com.github.linyuzai.concept.sample.event.rabbitmq.custom;

import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointConfigurer;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CustomRabbitEventEndpointConfigurer implements RabbitEventEndpointConfigurer {

    @Override
    public void configure(RabbitEventEndpoint endpoint) {
        //自定义配置
    }
}
