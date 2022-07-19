package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.lang.reflect.Type;

public interface EventSubscriber {

    void subscribe(Type type, EventEndpoint endpoint, EventContext context);
}
