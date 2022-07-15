package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaEventPublisher extends EventPublisher {

    @Override
    default void publish(Object event, EventPublishEndpoint endpoint) {
        if (endpoint instanceof KafkaEventPublishEndpoint) {
            publish(event, (KafkaEventPublishEndpoint) endpoint);
        }
    }

    default void publish(Object event, KafkaEventPublishEndpoint endpoint) {
        publish(event, endpoint.getTemplate());
    }

    void publish(Object event, KafkaTemplate<Object, Object> template);
}
