package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link Source} 压缩相关的事件的父类
 * <p>
 * Super class of {@link Source} compression related events
 */
@Getter
public abstract class AbstractCompressSourceEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    public AbstractCompressSourceEvent(DownloadContext context, @NonNull Source source, String message) {
        super(context);
        this.source = source;
        setMessage(message);
    }
}
