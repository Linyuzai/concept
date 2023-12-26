package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * {@link Source} 的资源释放时会发布该事件。
 */
@Getter
public class SourceReleasedEvent extends AbstractSourceEvent {

    public SourceReleasedEvent(DownloadContext context, Source source) {
        super(context, source);
    }
}
