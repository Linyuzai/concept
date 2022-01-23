package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * 默认的下载上下文工厂 / Default factory
 */
@AllArgsConstructor
public class DefaultDownloadContextFactory implements DownloadContextFactory {

    @NonNull
    private List<DownloadContextInitializer> initializers;

    @NonNull
    private List<DownloadContextDestroyer> destroyers;

    /**
     * 直接创建一个上下文，没有额外的处理 / Create a context directly without additional processing
     *
     * @param options 下载操作参数 / Options of download
     * @return 下载上下文 / Context of download
     */
    @Override
    public DownloadContext create(DownloadOptions options) {
        DefaultDownloadContext context = new DefaultDownloadContext(UUID.randomUUID().toString(), options);
        context.setInitializers(initializers);
        context.setDestroyers(destroyers);
        return context;
    }
}
