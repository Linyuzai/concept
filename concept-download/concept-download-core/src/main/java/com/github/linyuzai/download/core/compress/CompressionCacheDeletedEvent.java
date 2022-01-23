package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.AbstractContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

@Getter
public class CompressionCacheDeletedEvent extends AbstractContextDestroyedEvent {

    private final Compression compression;

    public CompressionCacheDeletedEvent(DownloadContext context, Compression compression) {
        super(context, "Compression cache deleted");
        this.compression = compression;
    }
}
