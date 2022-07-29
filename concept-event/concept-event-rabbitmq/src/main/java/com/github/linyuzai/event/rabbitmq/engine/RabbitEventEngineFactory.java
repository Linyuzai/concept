package com.github.linyuzai.event.rabbitmq.engine;

import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

public interface RabbitEventEngineFactory {

    RabbitEventEngine create(RabbitEventProperties properties);
}
