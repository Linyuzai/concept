package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * 当使用 {@link FileCompression} 时会发布该事件。
 * <p>
 * This event is published when {@link FileCompression} is used.
 */
@Getter
public class SourceFileCompressedEvent extends AbstractSourceCompressedEvent {

    /**
     * 文件路径。
     * <p>
     * File path.
     */
    private final String filePath;

    public SourceFileCompressedEvent(DownloadContext context, Source source, String filePath) {
        super(context, source, "Compress source with file " + filePath);
        this.filePath = filePath;
    }
}
