package com.github.linyuzai.event.rabbitmq.inherit;

import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

public interface RabbitInheritHandler {

    void inherit(RabbitEventProperties properties);
}
