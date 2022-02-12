package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

public class WebClientSourceCreatedEvent extends AbstractCreateSourceEvent {

    public WebClientSourceCreatedEvent(DownloadContext context, WebClientSource source) {
        super(context, source);
    }
}
