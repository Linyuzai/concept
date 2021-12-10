package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import lombok.AllArgsConstructor;

import java.io.IOException;

/**
 * 下载源处理拦截器
 */
@AllArgsConstructor
public class CreateSourceInterceptor implements DownloadInterceptor, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceFactoryAdapter sourceFactoryAdapter;

    /**
     * 将所有需要下载的数据对象转换为下载源
     *
     * @param context 下载上下文
     * @param chain   下载链
     */
    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        Object source = context.getOptions().getSource();
        SourceFactory factory = sourceFactoryAdapter.getOriginalSourceFactory(source, context);
        context.set(Source.class, factory.create(source, context));
        chain.next(context);
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
