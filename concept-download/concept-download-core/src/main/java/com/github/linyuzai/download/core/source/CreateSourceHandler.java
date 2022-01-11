package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.handler.AutomaticDownloadHandler;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 下载源处理拦截器
 */
@AllArgsConstructor
public class CreateSourceHandler implements AutomaticDownloadHandler, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceFactoryAdapter sourceFactoryAdapter;

    /**
     * 将所有需要下载的数据对象转换为下载源
     *
     * @param context 下载上下文
     */
    @Override
    public void doHandle(DownloadContext context) {
        Object source = context.getOptions().getSource();
        SourceFactory factory = sourceFactoryAdapter.getFactory(source, context);
        Source sources = factory.create(source, context);
        context.set(Source.class, Mono.just(sources));
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
