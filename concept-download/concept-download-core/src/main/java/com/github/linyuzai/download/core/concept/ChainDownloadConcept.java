package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChainImpl;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * 基于链式拦截的下载执行器
 */
@Getter
public class ChainDownloadConcept implements DownloadConcept {

    private final DownloadContextFactory contextFactory;

    /**
     * 拦截器
     */
    private final List<DownloadInterceptor> interceptors;

    public ChainDownloadConcept(DownloadContextFactory contextFactory, List<DownloadInterceptor> interceptors) {
        this.contextFactory = contextFactory;
        this.interceptors = interceptors;
        this.interceptors.sort(Comparator.comparingInt(DownloadInterceptor::getOrder));
    }

    /**
     * 执行下载拦截链
     */
    @Override
    public void download(DownloadOptions options) throws IOException {
        DownloadContext context = contextFactory.create(options);
        new DownloadInterceptorChainImpl(0, interceptors).next(context);
    }
}
