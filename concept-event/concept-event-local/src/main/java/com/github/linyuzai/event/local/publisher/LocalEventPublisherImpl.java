package com.github.linyuzai.event.local.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.local.endpoint.LocalEventEndpoint;

public class LocalEventPublisherImpl extends LocalEventPublisher {

    @Override
    public void doPublish(Object event, LocalEventEndpoint endpoint, EventContext context) {
        for (LocalEventEndpoint.ListenerContainer container : endpoint.getContainers()) {
            container.consume(event);
        }
    }
}
