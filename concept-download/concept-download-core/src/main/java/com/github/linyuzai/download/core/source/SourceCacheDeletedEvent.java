package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

@Getter
public class SourceCacheDeletedEvent extends AbstractDestroyContextEvent {

    private final Source source;

    public SourceCacheDeletedEvent(DownloadContext context, Source source) {
        super(context, "Source cache deleted");
        this.source = source;
    }
}
