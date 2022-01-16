package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.order.OrderProvider;

public interface DownloadEventListener extends OrderProvider {

    void onEvent(Object event);
}
