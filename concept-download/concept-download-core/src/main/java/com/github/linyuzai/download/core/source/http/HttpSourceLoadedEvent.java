package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.AbstractSourceLoadedEvent;

public class HttpSourceLoadedEvent extends AbstractSourceLoadedEvent {

    public HttpSourceLoadedEvent(DownloadContext context, HttpSource source) {
        super(context, source, "Load " + source.getDescription() + " using Http(s)URLConnection");
    }
}
