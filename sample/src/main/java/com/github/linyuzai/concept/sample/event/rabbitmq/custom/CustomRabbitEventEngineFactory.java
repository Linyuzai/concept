package com.github.linyuzai.concept.sample.event.rabbitmq.custom;

import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngineFactory;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import org.springframework.stereotype.Component;

//@Component
public class CustomRabbitEventEngineFactory implements RabbitEventEngineFactory {

    @Override
    public RabbitEventEngine create(RabbitEventProperties config) {
        //自定义
        return null;
    }
}
