package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * 压缩使用缓存时会发布该事件。
 */
@Getter
public class SourceCompressedCacheUsedEvent extends AbstractCompressSourceEvent {

    /**
     * 缓存路径。
     */
    private final String cache;

    public SourceCompressedCacheUsedEvent(DownloadContext context, Source source, String cache) {
        super(context, source, "Compress source using cache " + cache);
        this.cache = cache;
    }
}
