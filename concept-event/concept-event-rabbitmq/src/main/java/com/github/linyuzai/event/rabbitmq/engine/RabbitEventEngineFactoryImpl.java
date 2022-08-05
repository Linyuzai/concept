package com.github.linyuzai.event.rabbitmq.engine;

import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

public class RabbitEventEngineFactoryImpl implements RabbitEventEngineFactory {

    @Override
    public RabbitEventEngine create(RabbitEventProperties properties) {
        RabbitEventEngine engine = new RabbitEventEngine();
        properties.apply(engine);
        return engine;
    }
}
