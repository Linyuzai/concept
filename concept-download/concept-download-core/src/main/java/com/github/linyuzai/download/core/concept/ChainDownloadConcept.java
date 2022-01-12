package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChainImpl;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.scheduler.DownloadScheduler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * 基于链式处理的下载接口实现 / Implementation of download interface based on chain of handler
 */
@Getter
@AllArgsConstructor
public class ChainDownloadConcept implements DownloadConcept {

    private final DownloadConfiguration configuration;

    private final DownloadContextFactory contextFactory;

    private final DownloadScheduler scheduler;

    private final DownloadReturnInterceptor returnInterceptor;

    private final List<DownloadHandler> handlers;

    /**
     * 通过下载配置获得一个下载参数 / Obtain a download parameter through the download configuration
     * 通过下载上下文工厂创建一个下载上下文 / Create a download context through the factory
     * 执行下载处理链 / Execute download handler chain
     *
     * @param function 可以通过下载配置来返回一个下载参数 / return an options from the configuration
     * @throws IOException I/O exception
     */
    @Override
    public Object download(Function<DownloadConfiguration, DownloadOptions> function) throws IOException {
        DownloadOptions options = function.apply(configuration);
        Mono<DownloadContext> context = contextFactory.create(options);
        Mono<Void> mono = context.publishOn(scheduler.getScheduler())
                .flatMap(it -> new DownloadHandlerChainImpl(0, handlers).next(it));
        return returnInterceptor.intercept(mono);
    }
}
