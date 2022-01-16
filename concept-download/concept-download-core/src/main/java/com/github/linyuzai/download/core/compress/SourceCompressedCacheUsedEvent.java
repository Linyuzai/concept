package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

@Getter
public class SourceCompressedCacheUsedEvent extends SourceCompressedEvent {

    private final String cache;

    public SourceCompressedCacheUsedEvent(DownloadContext context, Source source, String cache) {
        super(context, source, null, "Compress source using cache " + cache);
        this.cache = cache;
    }
}
