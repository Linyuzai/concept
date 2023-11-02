package com.github.linyuzai.download.core.event;

/**
 * {@link DownloadEvent} 的监听器。
 */
public interface DownloadEventListener {

    /**
     * 事件监听回调。
     *
     * @param event 事件
     */
    void onEvent(Object event);
}
