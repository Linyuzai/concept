package com.github.linyuzai.download.core.source.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

/**
 * {@link ClassPathSource} 创建完成后会发布该事件。
 */
public class ClassPathSourceCreatedEvent extends AbstractCreateSourceEvent {

    public ClassPathSourceCreatedEvent(DownloadContext context, ClassPathSource source) {
        super(context, source);
    }
}
