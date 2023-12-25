package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link Source} 加载完成后会发布该事件。
 */
@Getter
public class SourceLoadedEvent extends DownloadContextEvent {

    /**
     * 被加载的 {@link Source}
     */
    @NonNull
    private final Source source;

    public SourceLoadedEvent(DownloadContext context, @NonNull Source source) {
        super(context);
        this.source = source;
    }
}
