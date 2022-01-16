package com.github.linyuzai.download.core.event;

import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Collection;

public class ApplicationDownloadEventPublisher extends DefaultDownloadEventPublisher
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
