package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.AbstractContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

@Getter
public class CompressionReleasedEvent extends AbstractContextDestroyedEvent {

    private final Compression compression;

    public CompressionReleasedEvent(DownloadContext context, Compression compression) {
        super(context, "Compression resource released");
        this.compression = compression;
    }
}
