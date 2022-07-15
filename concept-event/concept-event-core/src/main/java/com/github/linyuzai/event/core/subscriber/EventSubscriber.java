package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;

public interface EventSubscriber {

    void subscribe(EventPublishEndpoint endpoint);

    void onEvent();
}
