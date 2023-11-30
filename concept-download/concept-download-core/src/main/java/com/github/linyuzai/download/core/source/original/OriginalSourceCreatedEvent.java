package com.github.linyuzai.download.core.source.original;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当需要下载的原始数据对象原本就是 {@link Source} 时会发布该事件。
 */
public class OriginalSourceCreatedEvent extends AbstractCreateSourceEvent {

    public OriginalSourceCreatedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
