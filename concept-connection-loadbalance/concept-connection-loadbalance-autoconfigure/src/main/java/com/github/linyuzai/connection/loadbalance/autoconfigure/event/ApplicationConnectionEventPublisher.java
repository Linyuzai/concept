package com.github.linyuzai.connection.loadbalance.autoconfigure.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

/**
 * 支持 {@link EventListener} 的事件发布器。
 * <p>
 * {@link ConnectionEventPublisher} supported {@link EventListener}.
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
