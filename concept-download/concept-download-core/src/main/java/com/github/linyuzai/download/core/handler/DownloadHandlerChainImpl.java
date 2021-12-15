package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * 下载拦截链实现
 */
@AllArgsConstructor
public class DownloadHandlerChainImpl implements DownloadHandlerChain {

    private final int index;

    private final List<DownloadHandler> handlers;

    @Override
    public void next(DownloadContext context) throws IOException {
        DownloadHandlerInterceptor interceptor = context.getOptions().getInterceptor();
        if (interceptor != null && index - 1 >= 0) {
            interceptor.intercept(handlers.get(index - 1), context);
        }
        if (index < handlers.size()) {
            DownloadHandler handler = handlers.get(index);
            DownloadHandlerChain chain = new DownloadHandlerChainImpl(index + 1, handlers);
            handler.handle(context, chain);
        }
    }
}
