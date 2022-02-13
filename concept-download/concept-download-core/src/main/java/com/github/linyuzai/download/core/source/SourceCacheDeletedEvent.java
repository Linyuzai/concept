package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * {@link Source} 的缓存删除时会发布该事件。
 */
@Getter
public class SourceCacheDeletedEvent extends AbstractDestroyContextEvent {

    /**
     * 被删除缓存的 {@link Source}
     */
    private final Source source;

    public SourceCacheDeletedEvent(DownloadContext context, Source source) {
        super(context, "Source cache deleted");
        this.source = source;
    }
}
