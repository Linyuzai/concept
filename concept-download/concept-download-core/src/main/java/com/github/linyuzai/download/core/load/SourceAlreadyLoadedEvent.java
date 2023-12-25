package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceEvent;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当加载已经加载过的 {@link Source} 会发布该事件。
 */
public class SourceAlreadyLoadedEvent extends AbstractSourceEvent {

    public SourceAlreadyLoadedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
