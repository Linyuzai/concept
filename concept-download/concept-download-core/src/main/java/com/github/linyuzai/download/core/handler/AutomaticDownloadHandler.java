package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;

public interface AutomaticDownloadHandler extends DownloadHandler {

    @Override
    default void handle(DownloadContext context, DownloadHandlerChain chain) throws IOException {
        doHandle(context);
        DownloadHandlerInterceptor interceptor = context.getOptions().getInterceptor();
        if (interceptor != null) {
            interceptor.intercept(this, context);
        }
        chain.next(context);
    }

    void doHandle(DownloadContext context) throws IOException;
}
