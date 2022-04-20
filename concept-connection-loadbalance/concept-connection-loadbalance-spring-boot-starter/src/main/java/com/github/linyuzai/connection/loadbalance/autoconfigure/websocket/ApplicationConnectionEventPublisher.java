package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.core.event.DefaultConnectionEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@AllArgsConstructor
public class ApplicationConnectionEventPublisher extends DefaultConnectionEventPublisher {

    private ApplicationEventPublisher publisher;

    @Override
    public void publish(Object event) {
        super.publish(event);
        publisher.publishEvent(event);
    }
}
