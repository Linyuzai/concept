package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.AbstractContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

@Getter
public class CompressionCacheDeletedEvent extends AbstractContextDestroyedEvent {

    private final Compression compression;

    public CompressionCacheDeletedEvent(DownloadContext context, Compression compression) {
        super(context, "Compression cache deleted");
        this.compression = compression;
    }
}
