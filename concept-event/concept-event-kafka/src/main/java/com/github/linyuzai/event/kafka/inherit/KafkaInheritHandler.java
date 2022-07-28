package com.github.linyuzai.event.kafka.inherit;

import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;

public interface KafkaInheritHandler {

    void inherit(KafkaEventProperties properties);
}
