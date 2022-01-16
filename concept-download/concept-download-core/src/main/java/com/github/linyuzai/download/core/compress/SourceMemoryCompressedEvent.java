package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public class SourceMemoryCompressedEvent extends SourceCompressedEvent {

    public SourceMemoryCompressedEvent(DownloadContext context, Source source) {
        super(context, source, null, "Compress source in memory");
    }
}
