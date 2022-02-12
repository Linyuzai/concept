package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * {@link DownloadEvent} 的监听器。
 */
public interface DownloadEventListener extends OrderProvider {

    /**
     * 事件监听回调。
     *
     * @param event 事件
     */
    void onEvent(Object event);
}
