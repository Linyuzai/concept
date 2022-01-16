package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.SourceLoadedEvent;

public class WebClientSourceLoadedEvent extends SourceLoadedEvent {

    public WebClientSourceLoadedEvent(DownloadContext context, WebClientSource source) {
        super(context, source, "Load " + source + " using WebClient(webflux)");
    }
}
