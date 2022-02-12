package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

import java.io.File;

/**
 * 当使用 {@link FileCompression} 文件压缩时会发布该事件。
 */
@Getter
public class SourceFileCompressionEvent extends AbstractCompressSourceEvent {

    /**
     * 压缩文件
     */
    private final File file;

    public SourceFileCompressionEvent(DownloadContext context, Source source, File file) {
        super(context, source, "Compress source with file " + file.getAbsolutePath());
        this.file = file;
    }
}
