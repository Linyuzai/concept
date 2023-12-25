package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当使用 {@link NoCompression} 不压缩时会发布该事件。
 */
public class SourceNoCompressionEvent extends AbstractCompressSourceEvent {

    public SourceNoCompressionEvent(DownloadContext context, Source source) {
        super(context, source, "Compression skipped");
    }
}
