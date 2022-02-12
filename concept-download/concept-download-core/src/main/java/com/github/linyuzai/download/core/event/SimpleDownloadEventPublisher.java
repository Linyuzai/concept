package com.github.linyuzai.download.core.event;

import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * {@link DownloadEventPublisher} 的简单实现，支持 {@link DownloadEventListener} 的监听机制。
 */
@AllArgsConstructor
public class SimpleDownloadEventPublisher implements DownloadEventPublisher {

    /**
     * {@link DownloadEventListener} 集合
     */
    private Collection<DownloadEventListener> listeners;

    @Override
    public void publish(Object event) {
        if (listeners != null) {
            listeners.forEach(it -> it.onEvent(event));
        }
    }
}
