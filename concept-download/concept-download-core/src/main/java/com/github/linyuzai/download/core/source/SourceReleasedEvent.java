package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link Source} 的资源释放时会发布该事件。
 */
public class SourceReleasedEvent extends AbstractSourceEvent {

    public SourceReleasedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
