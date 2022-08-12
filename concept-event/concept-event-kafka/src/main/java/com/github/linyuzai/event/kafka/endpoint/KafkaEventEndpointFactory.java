package com.github.linyuzai.event.kafka.endpoint;

import com.github.linyuzai.event.core.endpoint.EventEndpointFactory;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;

/**
 * Kafka 事件端点工厂
 */
public interface KafkaEventEndpointFactory extends
        EventEndpointFactory<KafkaEventProperties.ExtendedKafkaProperties, KafkaEventEngine, KafkaEventEndpoint> {
}
