package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;

public interface EventSubscriber {

    void subscribe(EventEndpoint endpoint);
}
