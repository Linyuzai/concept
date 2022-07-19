package com.github.linyuzai.event.core.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

public interface EventPublisher {

    void publish(Object event, EventEndpoint endpoint, EventContext context);
}
