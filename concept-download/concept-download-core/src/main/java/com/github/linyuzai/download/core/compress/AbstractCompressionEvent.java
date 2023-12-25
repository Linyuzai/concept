package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

/**
 * {@link Compression} 事件。
 */
@Getter
public class AbstractCompressionEvent extends DownloadContextEvent {

    private final Compression compression;

    public AbstractCompressionEvent(DownloadContext context, Compression compression) {
        super(context);
        this.compression = compression;
    }
}
