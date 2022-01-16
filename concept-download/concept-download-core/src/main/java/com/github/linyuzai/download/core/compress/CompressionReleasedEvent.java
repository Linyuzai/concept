package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

@Getter
public class CompressionReleasedEvent extends DownloadContextEvent {

    private final Compression compression;

    public CompressionReleasedEvent(DownloadContext context, Compression compression) {
        super(context);
        this.compression = compression;
        setMessage("Compression resource released");
    }
}
