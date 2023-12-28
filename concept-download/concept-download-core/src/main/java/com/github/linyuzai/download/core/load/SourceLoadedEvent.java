package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceEvent;
import com.github.linyuzai.download.core.source.Source;

/**
 * {@link Source} 加载完成后会发布该事件。
 */
public class SourceLoadedEvent extends AbstractSourceEvent {

    public SourceLoadedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
