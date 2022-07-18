package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaEventPublisher extends EventPublisher {

    @Override
    default void publish(Object event, EventEndpoint endpoint) {
        if (endpoint instanceof KafkaEventEndpoint) {
            publish(event, (KafkaEventEndpoint) endpoint);
        }
    }

    void publish(Object event, KafkaEventEndpoint endpoint);
}
