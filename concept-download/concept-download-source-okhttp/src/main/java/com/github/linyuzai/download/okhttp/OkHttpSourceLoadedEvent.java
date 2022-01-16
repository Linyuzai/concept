package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.SourceLoadedEvent;

public class OkHttpSourceLoadedEvent extends SourceLoadedEvent {

    public OkHttpSourceLoadedEvent(DownloadContext context, OkHttpSource source) {
        super(context, source, "Load " + source + " using OkHttpClient");
    }
}
