package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public class SourceMemoryCompressedEvent extends AbstractSourceCompressedEvent {

    public SourceMemoryCompressedEvent(DownloadContext context, Source source) {
        super(context, source, "Compress source in memory");
    }
}
