package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.event.DefaultConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.event.EventPublishErrorHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.function.BiConsumer;

@AllArgsConstructor
public class ApplicationConnectionEventPublisher extends DefaultConnectionEventPublisher {

    private ApplicationEventPublisher publisher;

    public ApplicationConnectionEventPublisher(ApplicationEventPublisher publisher, EventPublishErrorHandler errorHandler) {
        super(errorHandler);
        this.publisher = publisher;
    }

    @Override
    public void publish(Object event) {
        super.publish(event);
        try {
            publisher.publishEvent(event);
        } catch (Throwable e) {
            handlePublishError(event, e);
        }
    }
}
