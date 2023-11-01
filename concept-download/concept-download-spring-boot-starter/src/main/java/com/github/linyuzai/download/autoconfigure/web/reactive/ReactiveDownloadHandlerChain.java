package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * {@link DownloadHandlerChain} 的默认实现。
 */
@AllArgsConstructor
public class ReactiveDownloadHandlerChain implements DownloadHandlerChain {

    /**
     * 下标，指定处理器位置
     */
    private int index;

    /**
     * 处理器列表
     */
    private final List<DownloadHandler> handlers;

    /**
     * 如果可以获得下一个处理器则调用，否则返回 {@link Mono#empty()}。
     *
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
                Mono<Void> mono = context.get(Mono.class);
                if (mono == null) {
                    return Mono.empty();
                }
                return mono;
            }
        });
    }

    @Getter
    @RequiredArgsConstructor
    public static class HandlerDelegate {

        private final DownloadHandler handler;

        public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
            Object handle = handler.handle(context, chain);
            if (handle instanceof Publisher<?>) {
                return Mono.from((Publisher<?>) handle).then();
            } else {
                return Mono.justOrEmpty(handle).then();
            }
        }
    }
}
