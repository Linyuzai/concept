package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link DownloadEvent} 发布器。
 *
 * @see SimpleDownloadEventPublisher
 */
public interface DownloadEventPublisher {

    /**
     * 发布事件。
     *
     * @param event 事件
     */
    void publish(Object event);

    static DownloadEventPublisher get(DownloadContext context) {
        return context.get(DownloadEventPublisher.class);
    }
}
