package com.github.linyuzai.event.core.publisher;

import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;

public interface EventPublisher {

    void publish(Object event, EventPublishEndpoint endpoint);
}
