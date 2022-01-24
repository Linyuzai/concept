package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.AbstractContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * 压缩文件的缓存删除时会发布该事件。
 * <p>
 * This event is published when the cache of compressed files is deleted.
 */
@Getter
public class CompressionCacheDeletedEvent extends AbstractContextDestroyedEvent {

    private final Compression compression;

    public CompressionCacheDeletedEvent(DownloadContext context, Compression compression) {
        super(context, "Compression cache deleted");
        this.compression = compression;
    }
}
