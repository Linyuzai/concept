package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractSourceCompressedEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    public AbstractSourceCompressedEvent(DownloadContext context, Source source) {
        this(context, source, "Source compressed");
    }

    public AbstractSourceCompressedEvent(DownloadContext context, @NonNull Source source, String message) {
        super(context);
        this.source = source;
        setMessage(message);
    }
}
