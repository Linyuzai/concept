package com.github.linyuzai.plugin.autoconfigure.event;

import com.github.linyuzai.plugin.core.event.DefaultPluginEventPublisher;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

/**
 * 支持 {@link EventListener} 的事件发布器。
 * <p>
 * {@link PluginEventPublisher} supported {@link EventListener}.
 */
@Getter
@RequiredArgsConstructor
public class ApplicationConnectionEventPublisher extends DefaultPluginEventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(Object event) {
        super.publish(event);
        publisher.publishEvent(event);
    }
}
