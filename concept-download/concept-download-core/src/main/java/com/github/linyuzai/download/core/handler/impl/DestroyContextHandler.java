package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.context.AfterContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 上下文销毁处理器 / A handler to destroy context
 * 在下载流程结束后执行 / After downloaded
 * 调用所有的上下文销毁器 / Call all destroyers {@link DownloadContextDestroyer#destroy(DownloadContext)}
 */
@AllArgsConstructor
public class DestroyContextHandler implements DownloadHandler {

    @NonNull
    private List<DownloadContextDestroyer> destroyers;

    /**
     * 遍历执行所有的上下文销毁器 / Iterate and call all destroyers
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        for (DownloadContextDestroyer destroyer : destroyers) {
            destroyer.destroy(context);
        }
        context.destroy();
        publisher.publish(new AfterContextDestroyedEvent(context));
        return chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_DESTROY_CONTEXT;
    }
}
