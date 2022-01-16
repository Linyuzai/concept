package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

import java.io.File;

@Getter
public class SourceLoadedCacheUsedEvent extends SourceLoadedEvent {

    private final String cache;

    public SourceLoadedCacheUsedEvent(DownloadContext context, Source source, String cache) {
        super(context, source, "Load " + source + " using cache " + cache);
        this.cache = cache;
    }
}
