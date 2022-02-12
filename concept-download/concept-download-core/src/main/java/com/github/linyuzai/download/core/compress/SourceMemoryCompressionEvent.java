package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当使用 {@link MemoryCompression} 内存压缩时会发布该事件。
 */
public class SourceMemoryCompressionEvent extends AbstractCompressSourceEvent {

    public SourceMemoryCompressionEvent(DownloadContext context, Source source) {
        super(context, source, "Compress source in memory");
    }
}
