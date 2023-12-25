package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * {@link Source} 加载使用缓存时会发布该事件。
 */
@Getter
public class SourceLoadedUsingCacheEvent extends AbstractSourceEvent {

    private final String cache;

    public SourceLoadedUsingCacheEvent(DownloadContext context, Source source, String cache) {
        super(context, source);
        this.cache = cache;
    }
}
