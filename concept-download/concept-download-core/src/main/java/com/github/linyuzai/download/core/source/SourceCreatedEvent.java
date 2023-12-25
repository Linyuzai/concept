package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link Source} 创建完成后会发布该事件。
 */
public class SourceCreatedEvent extends AbstractSourceEvent {

    public SourceCreatedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
