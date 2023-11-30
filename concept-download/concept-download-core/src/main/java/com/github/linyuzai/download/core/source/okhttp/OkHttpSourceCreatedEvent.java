package com.github.linyuzai.download.core.source.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

/**
 * {@link OkHttpSource} 创建完成后会发布该事件。
 */
public class OkHttpSourceCreatedEvent extends AbstractCreateSourceEvent {

    public OkHttpSourceCreatedEvent(DownloadContext context, OkHttpSource source) {
        super(context, source);
    }
}
