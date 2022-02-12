package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * {@link Source} 压缩完成后会发布该事件。
 */
@Getter
public class AfterSourceCompressedEvent extends AbstractCompressSourceEvent {

    /**
     * 压缩后的对象
     */
    private final Compression compression;

    public AfterSourceCompressedEvent(DownloadContext context, Source source, Compression compression) {
        super(context, source, "Source compressed");
        this.compression = compression;
    }
}
