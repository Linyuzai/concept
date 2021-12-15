package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.InitializeContextHandler;

public interface EnumDownloadHandlerInterceptor extends DownloadHandlerInterceptor {

    @Override
    default void intercept(DownloadHandler handler, DownloadContext context) {
        if (handler instanceof InitializeContextHandler) {
            onContextInitialized(context);
        } else {
            onOtherHandled(handler, context);
        }
    }

    default void onContextInitialized(DownloadContext context) {

    }

    default void onOtherHandled(DownloadHandler handler, DownloadContext context) {

    }
}
