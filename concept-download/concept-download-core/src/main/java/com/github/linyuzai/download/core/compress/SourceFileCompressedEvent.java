package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

@Getter
public class SourceFileCompressedEvent extends AbstractSourceCompressedEvent {

    private final String file;

    public SourceFileCompressedEvent(DownloadContext context, Source source, String file) {
        super(context, source, "Compress source with file " + file);
        this.file = file;
    }
}
