package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link Compression} 的资源释放时会发布该事件。
 */
public class CompressionReleasedEvent extends AbstractCompressionEvent {

    public CompressionReleasedEvent(DownloadContext context, Compression compression) {
        super(context, compression);
    }
}
