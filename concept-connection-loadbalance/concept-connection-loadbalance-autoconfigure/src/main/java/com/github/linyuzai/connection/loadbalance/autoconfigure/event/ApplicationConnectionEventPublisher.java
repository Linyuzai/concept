package com.github.linyuzai.connection.loadbalance.autoconfigure.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEventPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 兼容 Spring Events 的事件发布器。
 * 可以使用 @EventListener 来监听发布的事件。
 * <p>
 * Support Spring Events.
 */
@Getter
@RequiredArgsConstructor
public class ApplicationConnectionEventPublisher extends AbstractConnectionEventPublisher {

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
