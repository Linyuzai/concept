package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * {@link DownloadEvent} 的监听器。
 */
public interface DownloadEventListener extends OrderProvider {

    void onEvent(Object event);
}
