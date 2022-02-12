package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * 当确定压缩格式时会发布该事件。
 */
@Getter
public class SourceCompressionFormatEvent extends AbstractCompressSourceEvent {

    /**
     * 压缩格式
     */
    private final String format;

    public SourceCompressionFormatEvent(DownloadContext context, Source source, String format) {
        super(context, source, "Compress source using " + format);
        this.format = format;
    }
}
