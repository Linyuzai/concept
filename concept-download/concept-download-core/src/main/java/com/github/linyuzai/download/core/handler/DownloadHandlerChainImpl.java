package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 下载处理链默认实现
 */
@AllArgsConstructor
public class DownloadHandlerChainImpl implements DownloadHandlerChain {

    private int index;

    private final List<DownloadHandler> handlers;

    /**
     * 对上一个处理器进行拦截 / Intercept the previous handler
     * 获取下一个处理器 / Get next handler
     * 执行处理器 / Execute handler
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public Mono<Void> next(DownloadContext context) {
        if (index < handlers.size()) {
            DownloadHandler handler = handlers.get(index);
            DownloadHandlerInterceptor interceptor = context.getOptions().getInterceptor();
            if (interceptor != null) {
                interceptor.intercept(handler, context);
            }
            DownloadHandlerChain chain = new DownloadHandlerChainImpl(index + 1, handlers);
            return handler.handle(context, chain);
        } else {
            return Mono.empty();
        }
    }
}
