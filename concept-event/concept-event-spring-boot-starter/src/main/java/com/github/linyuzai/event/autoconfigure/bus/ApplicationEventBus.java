package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.AbstractEventBus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;

@Getter
@AllArgsConstructor
public class ApplicationEventBus extends AbstractEventBus {

    private ApplicationEventPublisher publisher;

    @Override
    public void onPublish(Object event) {
        publisher.publishEvent(event);
    }

    @Override
    public void onEvent(Object event) {
        publisher.publishEvent(event);
    }
}
