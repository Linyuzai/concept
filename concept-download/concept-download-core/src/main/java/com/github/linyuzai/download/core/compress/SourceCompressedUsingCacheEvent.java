package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * {@link Source} 压缩使用缓存时会发布该事件。
 */
@Getter
public class SourceCompressedUsingCacheEvent extends AbstractSourceEvent {

    /**
     * 缓存
     */
    private final String cache;

    public SourceCompressedUsingCacheEvent(DownloadContext context, Source source, String cache) {
        super(context, source);
        this.cache = cache;
    }
}
