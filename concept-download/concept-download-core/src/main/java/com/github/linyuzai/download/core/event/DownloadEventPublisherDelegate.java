package com.github.linyuzai.download.core.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * {@link DownloadEventPublisher} 的委派类，用于临时的组合监听器调用。
 */
@Getter
@RequiredArgsConstructor
public class DownloadEventPublisherDelegate implements DownloadEventPublisher {

    /**
     * 真实的 {@link DownloadEventPublisher}
     */
    @NonNull
    private final DownloadEventPublisher delegate;

    /**
     * 额外的 {@link DownloadEventListener}
     */
    @NonNull
    private final DownloadEventListener listener;

    /**
     * 同时回调额外的 {@link DownloadEventListener}。
     *
     * @param event 事件
     */
    @Override
    public void publish(Object event) {
        delegate.publish(event);
        listener.onEvent(event);
    }
}
