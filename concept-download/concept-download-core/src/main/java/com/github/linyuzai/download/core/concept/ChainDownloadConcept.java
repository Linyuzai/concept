package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChainImpl;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.order.OrderProvider;
import lombok.Getter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 基于链式拦截的下载执行器
 */
@Getter
public class ChainDownloadConcept implements DownloadConcept {

    private final DownloadConfiguration configuration;

    private final List<DownloadConfigurer> configurers;

    private final DownloadContextFactory contextFactory;

    /**
     * 拦截器
     */
    private final List<DownloadInterceptor> interceptors;

    public ChainDownloadConcept(DownloadConfiguration configuration, List<DownloadConfigurer> configurers,
                                DownloadContextFactory contextFactory, List<DownloadInterceptor> interceptors) {
        this.configuration = configuration;
        this.configurers = configurers;
        this.contextFactory = contextFactory;
        this.interceptors = interceptors;
        this.interceptors.sort(Comparator.comparingInt(OrderProvider::getOrder));
        this.configurers.forEach(it -> it.configure(this.configuration));
    }

    @Override
    public void download(Function<DownloadConfiguration, DownloadOptions> function) throws IOException {
        DownloadOptions options = function.apply(configuration);
        DownloadContext context = contextFactory.create(options);
        new DownloadInterceptorChainImpl(0, interceptors).next(context);
    }
}
