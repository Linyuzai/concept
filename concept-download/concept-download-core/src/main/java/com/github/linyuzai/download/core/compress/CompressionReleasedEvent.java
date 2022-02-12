package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * {@link Compression} 的资源释放时会发布该事件。
 */
@Getter
public class CompressionReleasedEvent extends AbstractDestroyContextEvent {

    /**
     * 被释放资源的 {@link Compression}
     */
    private final Compression compression;

    public CompressionReleasedEvent(DownloadContext context, Compression compression) {
        super(context, "Compression resource released");
        this.compression = compression;
    }
}
