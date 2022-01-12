package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 上下文初始化处理器 / A handler to init context
 * 在上下文创建之后执行 / After context creation
 * 调用所有的上下文初始化器 / Call all initializers {@link DownloadContextInitializer#initialize(DownloadContext)}
 */
@AllArgsConstructor
public class InitializeContextHandler implements DownloadHandler {

    @NonNull
    private List<DownloadContextInitializer> initializers;

    /**
     * 遍历执行所有的上下文初始化器 / Iterate and call all initializers
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        for (DownloadContextInitializer initializer : initializers) {
            initializer.initialize(context);
        }
        return chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_INITIALIZE_CONTEXT;
    }
}
