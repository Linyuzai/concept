package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * 当使用 {@link java.util.zip.ZipOutputStream} 进行压缩时会发布该事件。
 * <p>
 * This event is published when {@link java.util.zip.ZipOutputStream} is used for compression.
 */
@Getter
public class SourceCompressionFormatEvent extends AbstractCompressSourceEvent {

    private final String format;

    public SourceCompressionFormatEvent(DownloadContext context, Source source, String format) {
        super(context, source, "Compress source using " + format);
        this.format = format;
    }
}
