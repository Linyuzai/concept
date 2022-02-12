package com.github.linyuzai.download.core.event;

/**
 * {@link DownloadEvent} 发布器。
 *
 * @see SimpleDownloadEventPublisher
 * @see ApplicationDownloadEventPublisher
 */
public interface DownloadEventPublisher {

    /**
     * 发布事件。
     *
     * @param event 事件
     */
    void publish(Object event);
}
