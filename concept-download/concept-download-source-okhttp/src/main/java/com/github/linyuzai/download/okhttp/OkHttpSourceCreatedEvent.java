package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceCreatedEvent;

public class OkHttpSourceCreatedEvent extends AbstractSourceCreatedEvent {

    public OkHttpSourceCreatedEvent(DownloadContext context, OkHttpSource source) {
        super(context, source);
    }
}
