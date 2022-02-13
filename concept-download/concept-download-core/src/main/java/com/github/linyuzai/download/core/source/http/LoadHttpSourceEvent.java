package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.AbstractLoadSourceEvent;

import java.net.HttpURLConnection;

/**
 * 使用 {@link HttpURLConnection} 加载时会发布该事件。
 */
public class LoadHttpSourceEvent extends AbstractLoadSourceEvent {

    public LoadHttpSourceEvent(DownloadContext context, HttpSource source) {
        super(context, source, "Load " + source.getDescription() + " using Http(s)URLConnection");
    }
}
