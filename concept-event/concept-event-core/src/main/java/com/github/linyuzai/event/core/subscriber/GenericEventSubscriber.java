package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

public interface GenericEventSubscriber<T> extends EventSubscriber {

    void onEvent(T event, EventEndpoint endpoint, EventContext context);
}
