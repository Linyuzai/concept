package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public class SourceZipCompressedEvent extends AbstractSourceCompressedEvent {

    public SourceZipCompressedEvent(DownloadContext context, Source source) {
        super(context, source, "Compress source using zip");
    }
}
