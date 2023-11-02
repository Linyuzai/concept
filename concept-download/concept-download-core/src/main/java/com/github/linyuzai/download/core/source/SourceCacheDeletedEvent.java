package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

/**
 * {@link Source} 的缓存删除时会发布该事件。
 */
@Getter
public class SourceCacheDeletedEvent extends DownloadContextEvent {

    /**
     * 被删除缓存的 {@link Source}
     */
    private final Source source;

    public SourceCacheDeletedEvent(DownloadContext context, Source source) {
        super(context);
        setMessage("Source cache deleted");
        this.source = source;
    }
}
