package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link Source} 事件。
 */
@Getter
public class AbstractSourceEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    public AbstractSourceEvent(DownloadContext context, @NonNull Source source) {
        super(context);
        this.source = source;
    }
}
