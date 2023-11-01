package com.github.linyuzai.download.autoconfigure.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

/**
 * {@link WebClientSource} 创建完成后会发布该事件。
 */
public class WebClientSourceCreatedEvent extends AbstractCreateSourceEvent {

    public WebClientSourceCreatedEvent(DownloadContext context, WebClientSource source) {
        super(context, source);
    }
}
