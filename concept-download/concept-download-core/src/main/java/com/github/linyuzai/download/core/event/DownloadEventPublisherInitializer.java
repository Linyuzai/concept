package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import lombok.AllArgsConstructor;

/**
 * {@link DownloadEventPublisher} 初始化器。
 * 将 {@link DownloadEventPublisher} 设置到 {@link DownloadContext} 中。
 */
@AllArgsConstructor
public class DownloadEventPublisherInitializer implements DownloadContextInitializer {

    private DownloadEventPublisher eventPublisher;

    @Override
    public void initialize(DownloadContext context) {
        DownloadEventListener listener = context.getOptions().getEventListener();
        DownloadEventPublisher publisher;
        if (listener == null) {
            publisher = eventPublisher;
        } else {
            publisher = new DownloadEventPublisherDelegate(eventPublisher, listener);
        }
        context.set(DownloadEventPublisher.class, publisher);
    }
}
