package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;

/**
 * 自动执行调用链 / Auto execute call chain
 * 支持拦截 / Support interception
 */
public interface AutomaticDownloadHandler extends DownloadHandler {

    @Override
    default void handle(DownloadContext context, DownloadHandlerChain chain) {
        doHandle(context);
        DownloadHandlerInterceptor interceptor = context.getOptions().getInterceptor();
        if (interceptor != null) {
            interceptor.intercept(this, context);
        }
        chain.next(context);
    }

    void doHandle(DownloadContext context);
}
