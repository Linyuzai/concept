package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当使用 {@link NoCompression} 时会发布该事件。
 * <p>
 * This event is published when {@link NoCompression} is used.
 */
public class SourceNoCompressionEvent extends AbstractCompressSourceEvent {

    public SourceNoCompressionEvent(DownloadContext context, Source source) {
        super(context, source, "Compress skipped");
    }
}
