package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.compress.CompressSourceHandler;
import com.github.linyuzai.download.core.context.DestroyContextHandler;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.InitializeContextHandler;
import com.github.linyuzai.download.core.loader.LoadSourceHandler;
import com.github.linyuzai.download.core.response.WriteResponseHandler;
import com.github.linyuzai.download.core.source.CreateSourceHandler;

public interface StandardDownloadHandlerInterceptor extends DownloadHandlerInterceptor {

    @Override
    default void intercept(DownloadHandler handler, DownloadContext context) {
        if (handler instanceof InitializeContextHandler) {
            onContextInitialized(context);
        } else if (handler instanceof CreateSourceHandler) {
            onSourceCreated(context);
        } else if (handler instanceof LoadSourceHandler) {
            onSourceLoaded(context);
        } else if (handler instanceof CompressSourceHandler) {
            onSourceCompressed(context);
        } else if (handler instanceof WriteResponseHandler) {
            onResponseWritten(context);
        } else if (handler instanceof DestroyContextHandler) {
            onContextDestroyed(context);
        } else {
            onOtherHandled(handler, context);
        }
    }

    default void onContextInitialized(DownloadContext context) {

    }

    default void onSourceCreated(DownloadContext context) {

    }

    default void onSourceLoaded(DownloadContext context) {

    }

    default void onSourceCompressed(DownloadContext context) {

    }

    default void onResponseWritten(DownloadContext context) {

    }

    default void onContextDestroyed(DownloadContext context) {

    }

    default void onOtherHandled(DownloadHandler handler, DownloadContext context) {

    }
}
