package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadEventPublisher;

/**
 * {@link DownloadContext} 销毁时发布 {@link AfterContextDestroyedEvent} 事件。
 */
public class PublishContextEventDestroyer implements DownloadContextDestroyer {

    /**
     * 发布一个 {@link AfterContextDestroyedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void destroy(DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new AfterContextDestroyedEvent(context));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
