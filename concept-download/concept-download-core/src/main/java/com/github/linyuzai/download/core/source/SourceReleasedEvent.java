package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

/**
 * {@link Source} 的资源释放时会发布该事件。
 */
@Getter
public class SourceReleasedEvent extends DownloadContextEvent {

    /**
     * 被释放资源的 {@link Source}
     */
    private final Source source;

    public SourceReleasedEvent(DownloadContext context, Source source) {
        super(context);
        setMessage("Source resource released");
        this.source = source;
    }
}
