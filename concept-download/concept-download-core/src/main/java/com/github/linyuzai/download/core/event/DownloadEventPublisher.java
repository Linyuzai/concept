package com.github.linyuzai.download.core.event;

/**
 * {@link DownloadEvent} 发布器。
 *
 * @see SimpleDownloadEventPublisher
 * @see ApplicationDownloadEventPublisher
 */
public interface DownloadEventPublisher {

    void publish(Object event);
}
