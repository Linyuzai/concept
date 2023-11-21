package com.github.linyuzai.download.autoconfigure.event;

import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.event.SimpleDownloadEventPublisher;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * 基于 {@link ApplicationEventPublisher} 的 {@link DownloadEventPublisher}，
 * 支持 {@link EventListener} 和 {@link DownloadEventListener} 的监听机制。
 */
public class ApplicationDownloadEventPublisher extends SimpleDownloadEventPublisher
        implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    public ApplicationDownloadEventPublisher(List<DownloadEventListener> listeners) {
        super(listeners);
    }

    /**
     * 会额外调用 {@link ApplicationEventPublisher} 发布事件。
     *
     * @param event 事件
     */
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
