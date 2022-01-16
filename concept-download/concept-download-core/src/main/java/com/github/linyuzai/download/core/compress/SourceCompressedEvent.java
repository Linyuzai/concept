package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SourceCompressedEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    private final Compression compression;

    public SourceCompressedEvent(DownloadContext context, Source source, Compression compression) {
        this(context, source, compression, "Source compressed");
    }

    public SourceCompressedEvent(DownloadContext context, @NonNull Source source, Compression compression, String message) {
        super(context);
        this.source = source;
        this.compression = compression;
        setMessage(message);
    }
}
