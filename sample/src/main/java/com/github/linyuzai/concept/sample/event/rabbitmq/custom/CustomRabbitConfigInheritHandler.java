package com.github.linyuzai.concept.sample.event.rabbitmq.custom;

import com.github.linyuzai.event.rabbitmq.inherit.RabbitConfigInheritHandler;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import org.springframework.stereotype.Component;

//@Component
public class CustomRabbitConfigInheritHandler implements RabbitConfigInheritHandler {

    @Override
    public void inherit(RabbitEventProperties config) {

    }
}
