package com.github.linyuzai.concept.sample.event.rabbitmq.custom;

import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointFactory;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import org.springframework.stereotype.Component;

//@Component
public class CustomRabbitEventEndpointFactory implements RabbitEventEndpointFactory {

    @Override
    public RabbitEventEndpoint create(String name, RabbitEventProperties.ExtendedRabbitProperties config, RabbitEventEngine engine) {
        //自定义
        return null;
    }
}
