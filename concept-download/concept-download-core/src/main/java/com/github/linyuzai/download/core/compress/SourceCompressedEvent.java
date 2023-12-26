package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceEvent;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.utils.DownloadUtils;
import lombok.Getter;

/**
 * {@link Source} 压缩完成后会发布该事件。
 */
@Getter
public class SourceCompressedEvent extends AbstractSourceEvent {

    /**
     * 压缩后的对象
     */
    private final Compression compression;

    public SourceCompressedEvent(DownloadContext context, Source source, Compression compression) {
        super(context, source);
        this.compression = compression;
    }
}
