package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

/**
 * 下载源处理拦截器
 */
@CommonsLog
@AllArgsConstructor
public class CreateSourceHandler implements DownloadHandler, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceFactoryAdapter sourceFactoryAdapter;

    /**
     * 将所有需要下载的数据对象转换为下载源
     *
     * @param context 下载上下文
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        log.info("Create download source");
        Object source = context.getOptions().getSource();
        SourceFactory factory = sourceFactoryAdapter.getFactory(source, context);
        return factory.create(source, context).flatMap(it -> {
            context.set(Source.class, it);
            return chain.next(context);
        });
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceFactoryAdapter.class, sourceFactoryAdapter);
    }

    @Override
    public void destroy(DownloadContext context) {
        boolean delete = context.getOptions().isSourceCacheDelete();
        if (delete) {
            Source source = context.get(Source.class);
            if (source != null) {
                source.deleteCache();
            }
        }
    }

    @Override
    public int getOrder() {
        return ORDER_CREATE_SOURCE;
    }
}
