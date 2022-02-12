package com.github.linyuzai.download.core.event;

import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;

import java.util.Collection;

/**
 * 基于 {@link ApplicationEventPublisher} 的 {@link DownloadEventPublisher}。
 * 支持 {@link EventListener} 和 {@link DownloadEventListener} 的监听机制。
 */
public class ApplicationDownloadEventPublisher extends SimpleDownloadEventPublisher
        implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    public ApplicationDownloadEventPublisher(Collection<DownloadEventListener> listeners) {
        super(listeners);
    }

    @Override
    public void publish(Object event) {
        super.publish(event);
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
