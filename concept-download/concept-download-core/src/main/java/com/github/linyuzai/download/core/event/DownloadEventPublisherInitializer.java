package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * {@link DownloadEventPublisher} 初始化器。
 * 在 {@link DownloadContext} 初始化时，设置 {@link DownloadEventPublisher}。
 */
@Getter
@RequiredArgsConstructor
public class DownloadEventPublisherInitializer implements DownloadContextInitializer {

    /**
     * 被设置的 {@link DownloadEventPublisher}
     */
    private final DownloadEventPublisher eventPublisher;

    /**
     * 如果设置了额外的 {@link DownloadEventListener}，
     * 使用 {@link DownloadEventPublisherDelegate} 回调额外的 {@link DownloadEventListener}，
     * 否则直接设置 {@link DownloadEventPublisher}。
     *
     * @param context {@link DownloadContext}
     */
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
