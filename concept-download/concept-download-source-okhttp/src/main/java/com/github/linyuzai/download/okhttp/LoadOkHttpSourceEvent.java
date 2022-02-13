package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.AbstractLoadSourceEvent;
import okhttp3.OkHttpClient;

/**
 * 使用 {@link OkHttpClient} 加载时会发布该事件。
 */
public class LoadOkHttpSourceEvent extends AbstractLoadSourceEvent {

    public LoadOkHttpSourceEvent(DownloadContext context, OkHttpSource source) {
        super(context, source, "Load " + source.getDescription() + " using OkHttpClient");
    }
}
