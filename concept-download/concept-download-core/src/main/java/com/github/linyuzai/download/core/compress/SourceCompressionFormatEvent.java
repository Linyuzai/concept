package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * 当确定 {@link Source} 压缩格式时会发布该事件。
 */
@Getter
public class SourceCompressionFormatEvent extends AbstractSourceEvent {

    /**
     * 压缩格式
     */
    private final String format;

    public SourceCompressionFormatEvent(DownloadContext context, Source source, String format) {
        super(context, source);
        this.format = format;
    }
}
