package com.github.linyuzai.connection.loadbalance.autoconfigure.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 支持 {@link org.springframework.context.event.EventListener} 的事件发布器
 */
@Getter
@RequiredArgsConstructor
public class ApplicationConnectionEventPublisher extends ConnectionEventPublisherImpl {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(Object event, ConnectionLoadBalanceConcept concept) {
        super.publish(event, concept);
        try {
            publisher.publishEvent(event);
        } catch (Throwable e) {
            handlePublishError(event, e, concept);
        }
    }
}
