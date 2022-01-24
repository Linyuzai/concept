package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * 压缩完成后会发布该事件。
 * <p>
 * This event is published after compression is complete.
 */
@Getter
public class AfterSourceCompressedEvent extends AbstractSourceCompressedEvent {

    private final Compression compression;

    public AfterSourceCompressedEvent(DownloadContext context, Source source, Compression compression) {
        super(context, source, "Source compressed");
        this.compression = compression;
    }
}
