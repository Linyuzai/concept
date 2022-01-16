package com.github.linyuzai.download.core.source.self;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;

public class SelfSourceCreatedEvent extends SourceCreatedEvent {

    public SelfSourceCreatedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
