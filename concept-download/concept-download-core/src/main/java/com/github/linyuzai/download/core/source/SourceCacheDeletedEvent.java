package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link Source} 的缓存删除时会发布该事件。
 */
public class SourceCacheDeletedEvent extends AbstractSourceEvent {

    public SourceCacheDeletedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
