package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * {@link DownloadHandlerChain} 的默认实现。
 */
@AllArgsConstructor
public class DownloadHandlerChainImpl implements DownloadHandlerChain {

    /**
     * 下标，指定处理器位置。
     */
    private int index;

    /**
     * 处理器列表。
     */
    private final List<DownloadHandler> handlers;

    /**
     * 如果可以获得下一个处理器则调用，
     * 否则返回 {@link Mono#empty()}
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public Mono<Void> next(DownloadContext context) {
        if (index < handlers.size()) {
            DownloadHandler handler = handlers.get(index);
            DownloadHandlerChain chain = new DownloadHandlerChainImpl(index + 1, handlers);
            return handler.handle(context, chain);
        } else {
            return Mono.empty();
        }
    }
}
