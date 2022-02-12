package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

/**
 * 压缩资源释放时会发布该事件。
 */
@Getter
public class CompressionReleasedEvent extends AbstractDestroyContextEvent {

    private final Compression compression;

    public CompressionReleasedEvent(DownloadContext context, Compression compression) {
        super(context, "Compression resource released");
        this.compression = compression;
    }
}
