package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

public interface KafkaEventSubscriber extends EventSubscriber {

    @Override
    default void subscribe(EventEndpoint endpoint) {
        if (endpoint instanceof KafkaEventEndpoint) {
            subscribe((KafkaEventEndpoint) endpoint);
        }
    }

    void subscribe(KafkaEventEndpoint endpoint);
}
