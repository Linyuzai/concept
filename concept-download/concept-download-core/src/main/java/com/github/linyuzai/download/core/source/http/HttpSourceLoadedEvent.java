package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.SourceLoadedEvent;

public class HttpSourceLoadedEvent extends SourceLoadedEvent {

    public HttpSourceLoadedEvent(DownloadContext context, HttpSource source) {
        super(context, source, "Load " + source + " using Http(s)URLConnection");
    }
}
