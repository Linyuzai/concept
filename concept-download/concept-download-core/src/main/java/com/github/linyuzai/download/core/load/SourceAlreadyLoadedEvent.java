package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当加载已经加载过的 {@link Source} 会发布该事件。
 */
public class SourceAlreadyLoadedEvent extends AbstractLoadSourceEvent {

    public SourceAlreadyLoadedEvent(DownloadContext context, Source source) {
        super(context, source, "Skip load " + source.getDescription() + " because it has already been loaded");
    }
}
