package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

/**
 * {@link TextSource} 创建完成后会发布该事件。
 */
public class TextSourceCreatedEvent extends AbstractCreateSourceEvent {

    public TextSourceCreatedEvent(DownloadContext context, TextSource source) {
        super(context, source);
    }
}
