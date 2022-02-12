package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * 压缩文件的缓存删除时会发布该事件。
 */
@Getter
public class CompressionCacheDeletedEvent extends AbstractDestroyContextEvent {

    private final Compression compression;

    public CompressionCacheDeletedEvent(DownloadContext context, Compression compression) {
        super(context, "Compression cache deleted");
        this.compression = compression;
    }
}
