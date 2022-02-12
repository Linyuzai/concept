package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link Source} 加载相关的事件的父类。
 */
@Getter
public abstract class AbstractLoadSourceEvent extends DownloadContextEvent {

    /**
     * 被加载的 {@link Source}
     */
    @NonNull
    private final Source source;

    public AbstractLoadSourceEvent(DownloadContext context, @NonNull Source source, String message) {
        super(context);
        this.source = source;
        setMessage(message);
    }
}
