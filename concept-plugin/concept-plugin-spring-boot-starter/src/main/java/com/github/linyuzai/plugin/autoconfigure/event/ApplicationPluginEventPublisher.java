package com.github.linyuzai.plugin.autoconfigure.event;

import com.github.linyuzai.plugin.core.event.DefaultPluginEventPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

/**
 * 支持 {@link EventListener} 的事件发布器
 */
@Getter
@RequiredArgsConstructor
public class ApplicationPluginEventPublisher extends DefaultPluginEventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(Object event) {
        super.publish(event);
        publisher.publishEvent(event);
    }
}
