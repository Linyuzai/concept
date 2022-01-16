package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;

public class OkHttpSourceCreatedEvent extends SourceCreatedEvent {

    public OkHttpSourceCreatedEvent(DownloadContext context, OkHttpSource source) {
        super(context, source);
    }
}
