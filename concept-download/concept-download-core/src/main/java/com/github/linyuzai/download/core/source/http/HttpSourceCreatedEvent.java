package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

public class HttpSourceCreatedEvent extends AbstractCreateSourceEvent {

    public HttpSourceCreatedEvent(DownloadContext context, HttpSource source) {
        super(context, source);
    }
}
