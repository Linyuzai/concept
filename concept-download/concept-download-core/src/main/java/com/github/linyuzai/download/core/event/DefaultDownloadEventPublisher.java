package com.github.linyuzai.download.core.event;

import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class DefaultDownloadEventPublisher implements DownloadEventPublisher {

    private Collection<DownloadEventListener> listeners;

    @Override
    public void publish(Object event) {
        if (listeners != null) {
            listeners.forEach(it -> it.onEvent(event));
        }
    }
}
