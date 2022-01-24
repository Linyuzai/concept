package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当使用 {@link MemoryCompression} 时会发布该事件。
 * <p>
 * This event is published when {@link MemoryCompression} is used.
 */
public class SourceMemoryCompressedEvent extends AbstractSourceCompressedEvent {

    public SourceMemoryCompressedEvent(DownloadContext context, Source source) {
        super(context, source, "Compress source in memory");
    }
}
