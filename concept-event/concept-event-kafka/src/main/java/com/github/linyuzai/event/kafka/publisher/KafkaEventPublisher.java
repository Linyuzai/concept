package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.publisher.AbstractEventPublisher;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;

/**
 * Kafka 事件发布器
 */
public abstract class KafkaEventPublisher extends AbstractEventPublisher<KafkaEventEndpoint> {
}
