package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceCreatedEvent;

public class HttpSourceCreatedEvent extends AbstractSourceCreatedEvent {

    public HttpSourceCreatedEvent(DownloadContext context, HttpSource source) {
        super(context, source);
    }
}
