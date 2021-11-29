package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import lombok.NonNull;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * 下载源处理拦截器
 */
public class PrepareSourceInterceptor implements DownloadInterceptor {

    /**
     * 下载源加载器
     */
    private final List<OriginalSourceFactory> factories;

    public PrepareSourceInterceptor(@NonNull List<OriginalSourceFactory> factories) {
        this.factories = factories;
        this.factories.sort(Comparator.comparingInt(OriginalSourceFactory::getOrder));
    }

    /**
     * 将所有需要下载的数据对象转换为下载源
     *
     * @param context 下载上下文
     * @param chain   下载链
     */
    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        Object source = context.getOptions().getSource();
        context.set(OriginalSourceFactory.class, factories);
        context.set(OriginalSource.class, OriginalSourceUtils.create(source, context, factories));
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_SOURCE_PREPARE;
    }
}
