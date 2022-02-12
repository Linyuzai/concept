package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.AbstractLoadSourceEvent;

public class WebClientSourceLoadedEvent extends AbstractLoadSourceEvent {

    public WebClientSourceLoadedEvent(DownloadContext context, WebClientSource source) {
        super(context, source, "Load " + source.getDescription() + " using WebClient(webflux)");
    }
}
