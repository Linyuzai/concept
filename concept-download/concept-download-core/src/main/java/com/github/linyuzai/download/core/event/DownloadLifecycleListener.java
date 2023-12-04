package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link DownloadContext} 初始化器，{@link DownloadContext} 初始化时会回调。
 */
public interface DownloadLifecycleListener extends DownloadEventListener {

    @Override
    default void onEvent(Object event) {
        if (event instanceof DownloadStartedEvent) {
            onStart(((DownloadStartedEvent) event).getContext());
        } else if (event instanceof DownloadCompletedEvent) {
            onComplete(((DownloadCompletedEvent) event).getContext());
        }
    }

    default void onStart(DownloadContext context) {

    }

    default void onComplete(DownloadContext context) {

    }
}
