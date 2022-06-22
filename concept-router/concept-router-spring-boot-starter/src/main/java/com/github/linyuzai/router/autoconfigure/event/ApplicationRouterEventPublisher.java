package com.github.linyuzai.router.autoconfigure.event;

import com.github.linyuzai.router.core.event.DefaultRouterEventPublisher;
import com.github.linyuzai.router.core.event.RouterEventListener;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

@Getter
public class ApplicationRouterEventPublisher extends DefaultRouterEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ApplicationRouterEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public ApplicationRouterEventPublisher(ApplicationEventPublisher eventPublisher,
                                           Collection<? extends RouterEventListener> listeners) {
        super(listeners);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(Object event) {
        super.publish(event);
        eventPublisher.publishEvent(event);
    }
}
