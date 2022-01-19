package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceCreatedEvent;

public class WebClientSourceCreatedEvent extends AbstractSourceCreatedEvent {

    public WebClientSourceCreatedEvent(DownloadContext context, WebClientSource source) {
        super(context, source);
    }
}
