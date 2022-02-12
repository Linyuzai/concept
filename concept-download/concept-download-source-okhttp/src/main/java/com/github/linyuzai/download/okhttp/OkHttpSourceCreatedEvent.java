package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

public class OkHttpSourceCreatedEvent extends AbstractCreateSourceEvent {

    public OkHttpSourceCreatedEvent(DownloadContext context, OkHttpSource source) {
        super(context, source);
    }
}
