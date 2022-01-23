package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 当使用 {@link java.util.zip.ZipOutputStream} 进行压缩时会发布该事件。
 * <p>
 * This event is published when {@link java.util.zip.ZipOutputStream} is used for compression.
 */
public class SourceZipCompressedEvent extends AbstractSourceCompressedEvent {

    public SourceZipCompressedEvent(DownloadContext context, Source source) {
        super(context, source, "Compress source using zip");
    }
}
