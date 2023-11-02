package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class AbstractDownloadConcept implements DownloadConcept {

    /**
     * 上下文工厂
     */
    private final DownloadContextFactory contextFactory;

    /**
     * 处理器
     */
    private final List<DownloadHandler> handlers;

    @Override
    public Object download(DownloadOptions options) {
        //创建上下文
        DownloadContext context = contextFactory.create(options);
        //初始化上下文
        context.initialize();
        List<DownloadHandler> filtered = handlers.stream()
                .filter(it -> it.support(context))
                .collect(Collectors.toList());
        //处理链
        return doDownload(context, filtered, context::destroy);
    }

    protected abstract Object doDownload(DownloadContext context, List<DownloadHandler> handlers, Runnable onComplete);
}
