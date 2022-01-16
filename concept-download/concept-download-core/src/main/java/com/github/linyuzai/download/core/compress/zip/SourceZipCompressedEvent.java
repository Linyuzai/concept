package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.SourceCompressedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public class SourceZipCompressedEvent extends SourceCompressedEvent {

    public SourceZipCompressedEvent(DownloadContext context, Source source) {
        super(context, source, null, "Compress source using zip");
    }
}
