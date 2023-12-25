package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link Compression} 的缓存删除时会发布该事件。
 */
public class CompressionCacheDeletedEvent extends AbstractCompressionEvent {

    public CompressionCacheDeletedEvent(DownloadContext context, Compression compression) {
        super(context, compression);
    }
}
