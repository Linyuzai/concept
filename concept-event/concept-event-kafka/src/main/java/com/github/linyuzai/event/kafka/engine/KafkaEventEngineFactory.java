package com.github.linyuzai.event.kafka.engine;

import com.github.linyuzai.event.core.engine.EventEngineFactory;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;

/**
 * Kafka 事件引擎工厂
 */
public interface KafkaEventEngineFactory extends EventEngineFactory<KafkaEventProperties, KafkaEventEngine> {
}
