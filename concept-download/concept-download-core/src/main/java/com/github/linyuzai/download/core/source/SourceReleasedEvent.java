package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * {@link Source} 的资源释放时会发布该事件。
 */
@Getter
public class SourceReleasedEvent extends AbstractDestroyContextEvent {

    /**
     * 被释放资源的 {@link Source}
     */
    private final Source source;

    public SourceReleasedEvent(DownloadContext context, Source source) {
        super(context, "Source resource released");
        this.source = source;
    }
}
