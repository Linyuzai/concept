package com.github.linyuzai.download.autoconfigure.web.servlet;

import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 基于 {@link DownloadHandlerChain} 的 {@link DownloadConcept} 实现。
 */
@Getter
@RequiredArgsConstructor
public class ServletDownloadConcept implements DownloadConcept {

    /**
     * 上下文工厂
     */
    private final DownloadContextFactory contextFactory;

    /**
     * 处理器
     */
    private final List<DownloadHandler> handlers;

    /**
     * 创建 {@link DownloadContext} 并初始化，
     * 调用 {@link DownloadHandlerChain} 处理下载数据，
     * 销毁 {@link DownloadContext}
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
        Object next = new ServletDownloadHandlerChain(handlers).next(context);
        context.destroy();
        return next;
    }
}
