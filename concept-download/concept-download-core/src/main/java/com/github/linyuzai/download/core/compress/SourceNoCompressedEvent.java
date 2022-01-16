package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public class SourceNoCompressedEvent extends SourceCompressedEvent {

    public SourceNoCompressedEvent(DownloadContext context, Source source) {
        super(context, source, null, "Compress skipped");
    }
}
