package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadEventPublisher;

/**
 * {@link DownloadContext} 初始化后发布 {@link AfterContextInitializedEvent} 事件。
 */
public class PublishContextEventInitializer implements DownloadContextInitializer {

    /**
     * 发布一个 {@link AfterContextInitializedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void initialize(DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new AfterContextInitializedEvent(context));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
