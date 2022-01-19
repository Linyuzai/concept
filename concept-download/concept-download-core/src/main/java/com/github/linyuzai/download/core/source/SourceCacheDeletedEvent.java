package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.AbstractContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

@Getter
public class SourceCacheDeletedEvent extends AbstractContextDestroyedEvent {

    private final Source source;

    public SourceCacheDeletedEvent(DownloadContext context, Source source) {
        super(context, "Source cache deleted");
        this.source = source;
    }
}
