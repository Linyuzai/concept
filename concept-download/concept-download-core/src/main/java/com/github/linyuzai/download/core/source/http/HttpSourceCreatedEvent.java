package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;

public class HttpSourceCreatedEvent extends SourceCreatedEvent {

    public HttpSourceCreatedEvent(DownloadContext context, HttpSource source) {
        super(context, source);
    }
}
