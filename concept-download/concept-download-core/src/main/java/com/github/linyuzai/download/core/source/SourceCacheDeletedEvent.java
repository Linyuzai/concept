package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

@Getter
public class SourceCacheDeletedEvent extends DownloadContextEvent {

    private final Source source;

    public SourceCacheDeletedEvent(DownloadContext context, Source source) {
        super(context);
        this.source = source;
        setMessage("Source cache deleted");
    }
}
