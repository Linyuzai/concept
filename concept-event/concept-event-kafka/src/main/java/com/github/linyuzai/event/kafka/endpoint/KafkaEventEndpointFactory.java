package com.github.linyuzai.event.kafka.endpoint;

import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;

public interface KafkaEventEndpointFactory {

    KafkaEventEndpoint create(String name,
                              KafkaEventProperties.ExtendedKafkaProperties properties,
                              KafkaEventEngine engine);
}
