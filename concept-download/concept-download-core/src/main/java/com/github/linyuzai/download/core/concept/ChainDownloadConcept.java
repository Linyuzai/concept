package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.handler.DownloadHandlerChainImpl;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * 基于 {@link DownloadHandlerChain} 的 {@link DownloadConcept} 实现。
 * <p>
 * Implementation of {@link DownloadConcept} based on {@link DownloadHandlerChain}.
 */
@Getter
@AllArgsConstructor
public class ChainDownloadConcept implements DownloadConcept {

    private final DownloadConfiguration configuration;

    private final DownloadContextFactory contextFactory;

    private final DownloadReturnInterceptor returnInterceptor;

    private final List<DownloadHandler> handlers;

    /**
     * 通过 {@link DownloadConfiguration} 获得 {@link DownloadOptions}，
     * 创建 {@link DownloadContext} 并初始化，
     * 调用 {@link DownloadHandlerChain} 处理下载数据，
     * 销毁 {@link DownloadContext}，
     * 通过 {@link DownloadReturnInterceptor} 返回最终值。
     * <p>
     * Obtain {@link DownloadOptions} through {@link DownloadConfiguration},
     * create {@link DownloadContext} and initialize it,
     * call {@link DownloadHandlerChain} to process the downloaded data,
     * destroy {@link DownloadContext},
     * and return the final value through {@link DownloadReturnInterceptor}.
     *
     * @param function 基于 {@link DownloadConfiguration} 返回 {@link DownloadOptions}
     *                 <p>
     *                 Return {@link DownloadOptions} based on {@link DownloadConfiguration}
     */
    @Override
    public Object download(Function<DownloadConfiguration, DownloadOptions> function) {
        DownloadOptions options = function.apply(configuration);
        DownloadContext context = contextFactory.create(options);
        context.initialize();
        Mono<Void> mono = new DownloadHandlerChainImpl(0, handlers)
                .next(context)
                .doAfterTerminate(context::destroy);
        return returnInterceptor.intercept(mono);
    }
}
