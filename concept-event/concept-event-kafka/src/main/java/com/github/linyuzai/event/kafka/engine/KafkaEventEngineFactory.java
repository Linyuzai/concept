package com.github.linyuzai.event.kafka.engine;

import com.github.linyuzai.event.core.engine.EventEngineFactory;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;

public interface KafkaEventEngineFactory extends EventEngineFactory<KafkaEventProperties, KafkaEventEngine> {
}
