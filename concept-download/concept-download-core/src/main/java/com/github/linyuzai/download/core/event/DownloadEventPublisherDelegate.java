package com.github.linyuzai.download.core.event;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class DownloadEventPublisherDelegate implements DownloadEventPublisher {

    @NonNull
    private final DownloadEventPublisher delegate;

    @NonNull
    private final DownloadEventListener listener;

    @Override
    public void publish(Object event) {
        delegate.publish(event);
        listener.onEvent(event);
    }
}
