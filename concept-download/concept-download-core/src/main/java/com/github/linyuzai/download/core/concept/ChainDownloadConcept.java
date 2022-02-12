package com.github.linyuzai.download.core.concept;

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

/**
 * 基于 {@link DownloadHandlerChain} 的 {@link DownloadConcept} 实现。
 */
@Getter
@AllArgsConstructor
public class ChainDownloadConcept implements DownloadConcept {

    /**
     * 上下文工厂
     */
    private final DownloadContextFactory contextFactory;

    /**
     * 返回拦截
     */
    private final DownloadReturnInterceptor returnInterceptor;

    /**
     * 处理器
     */
    private final List<DownloadHandler> handlers;

    /**
     * 创建 {@link DownloadContext} 并初始化，
     * 调用 {@link DownloadHandlerChain} 处理下载数据，
     * 销毁 {@link DownloadContext}，
     * 通过 {@link DownloadReturnInterceptor} 返回最终值。
     *
     * @param options {@link DownloadOptions}
     */
    @Override
    public Object download(DownloadOptions options) {
        //创建上下文
        DownloadContext context = contextFactory.create(options);
        //初始化上下文
        context.initialize();
        //处理链
        Mono<Void> mono = new DownloadHandlerChainImpl(0, handlers)
                .next(context)
                //最后销毁上下文
                .doAfterTerminate(context::destroy);
        //拦截返回值
        return returnInterceptor.intercept(mono);
    }
}
