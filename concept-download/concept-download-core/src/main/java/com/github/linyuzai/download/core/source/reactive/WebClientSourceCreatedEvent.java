package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;

public class WebClientSourceCreatedEvent extends SourceCreatedEvent {

    public WebClientSourceCreatedEvent(DownloadContext context, WebClientSource source) {
        super(context, source);
    }
}
