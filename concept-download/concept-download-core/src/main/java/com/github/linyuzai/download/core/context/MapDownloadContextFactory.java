package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 默认的下载上下文工厂 / Default factory
 */
public class MapDownloadContextFactory implements DownloadContextFactory {

    /**
     * 直接创建一个上下文，没有额外的处理 / Create a context directly without additional processing
     *
     * @param options 下载操作参数 / Options of download
     * @return 下载上下文 / Context of download
     */
    @Override
    public DownloadContext create(DownloadOptions options) {
        return new MapDownloadContext(options);
    }
}
