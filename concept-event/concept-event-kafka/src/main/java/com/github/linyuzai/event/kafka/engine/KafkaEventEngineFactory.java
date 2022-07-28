package com.github.linyuzai.event.kafka.engine;

import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;

public interface KafkaEventEngineFactory {

    KafkaEventEngine create(KafkaEventProperties properties);
}
