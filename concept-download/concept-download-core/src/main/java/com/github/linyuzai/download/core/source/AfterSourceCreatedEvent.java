package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * {@link Source} 创建完成后会发布该事件。
 */
@Getter
public class AfterSourceCreatedEvent extends AbstractCreateSourceEvent {

    public AfterSourceCreatedEvent(DownloadContext context, Source source) {
        super(context, source, "Source created");
    }
}
