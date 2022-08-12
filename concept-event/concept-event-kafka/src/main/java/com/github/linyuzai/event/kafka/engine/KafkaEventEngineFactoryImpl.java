package com.github.linyuzai.event.kafka.engine;

import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;

/**
 * Kafka 事件引擎工厂实现
 */
public class KafkaEventEngineFactoryImpl implements KafkaEventEngineFactory {

    @Override
    public KafkaEventEngine create(KafkaEventProperties properties) {
        KafkaEventEngine engine = new KafkaEventEngine();
        properties.apply(engine);
        return engine;
    }
}
