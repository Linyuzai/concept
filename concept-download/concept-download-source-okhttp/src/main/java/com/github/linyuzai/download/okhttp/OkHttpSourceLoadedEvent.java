package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.AbstractLoadSourceEvent;

public class OkHttpSourceLoadedEvent extends AbstractLoadSourceEvent {

    public OkHttpSourceLoadedEvent(DownloadContext context, OkHttpSource source) {
        super(context, source, "Load " + source.getDescription() + " using OkHttpClient");
    }
}
