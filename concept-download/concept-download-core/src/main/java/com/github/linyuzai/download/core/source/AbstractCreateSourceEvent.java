package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link Source} 创建相关的事件的父类。
 */
@Getter
public abstract class AbstractCreateSourceEvent extends DownloadContextEvent {

    /**
     * 创建的 {@link Source}
     */
    @NonNull
    private final Source source;

    public AbstractCreateSourceEvent(DownloadContext context, @NonNull Source source) {
        this(context, source, "Create " + source.getDescription());
    }

    public AbstractCreateSourceEvent(DownloadContext context, @NonNull Source source, String message) {
        super(context);
        this.source = source;
        setMessage(message);
    }
}
