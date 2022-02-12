package com.github.linyuzai.download.core.source.self;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;
import com.github.linyuzai.download.core.source.Source;

public class SelfSourceCreatedEvent extends AbstractCreateSourceEvent {

    public SelfSourceCreatedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
