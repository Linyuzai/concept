package com.github.linyuzai.event.rabbitmq.engine;

import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

/**
 * RabbitMQ 事件引擎工厂实现
 */
public class RabbitEventEngineFactoryImpl implements RabbitEventEngineFactory {

    @Override
    public RabbitEventEngine create(RabbitEventProperties properties) {
        RabbitEventEngine engine = new RabbitEventEngine();
        properties.apply(engine);
        return engine;
    }
}
