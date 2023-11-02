package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

/**
 * {@link Compression} 的缓存删除时会发布该事件。
 */
@Getter
public class CompressionCacheDeletedEvent extends DownloadContextEvent {

    /**
     * 删除缓存的 {@link Compression}
     */
    private final Compression compression;

    public CompressionCacheDeletedEvent(DownloadContext context, Compression compression) {
        super(context);
        setMessage("Compression cache deleted");
        this.compression = compression;
    }
}
