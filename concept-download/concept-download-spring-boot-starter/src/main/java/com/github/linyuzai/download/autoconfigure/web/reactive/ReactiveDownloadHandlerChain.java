package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * {@link DownloadHandlerChain} 的默认实现。
 */
@Getter
@RequiredArgsConstructor
public class ReactiveDownloadHandlerChain implements DownloadHandlerChain {

    /**
     * 下标，指定处理器位置
     */
    private final int index;

    /**
     * 处理器列表
     */
    private final List<DownloadHandler> handlers;

    /**
     * @param context {@link DownloadContext}
     */
    @Override
    public Object next(DownloadContext context) {
        return Mono.defer(() -> {
            if (index < handlers.size()) {
                HandlerDelegate handler = new HandlerDelegate(handlers.get(index));
                DownloadHandlerChain chain = new ReactiveDownloadHandlerChain(index + 1, handlers);
                return handler.handle(context, chain);
            } else {
                return Mono.empty();
            }
        });
    }

    @Getter
    @RequiredArgsConstructor
    public static class HandlerDelegate {

        private final DownloadHandler handler;

        public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
            try {
                Object handle = handler.handle(context, chain);
                if (handle instanceof Publisher<?>) {
                    return Mono.from((Publisher<?>) handle).then();
                } else {
                    return Mono.justOrEmpty(handle).then();
                }
            } catch (Throwable e) {
                return Mono.error(e);
            }
        }
    }
}
