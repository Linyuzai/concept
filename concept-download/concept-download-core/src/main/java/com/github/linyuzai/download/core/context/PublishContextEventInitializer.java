package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadEventPublisher;

/**
 * {@link DownloadContext} 初始化后发布对应的事件。
 * <p>
 * Publish the corresponding event after {@link DownloadContext} initialization.
 */
public class PublishContextEventInitializer implements DownloadContextInitializer {

    /**
     * 发布一个 {@link AfterContextInitializedEvent} 事件。
     * <p>
     * Publish an {@link AfterContextInitializedEvent} event.
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
