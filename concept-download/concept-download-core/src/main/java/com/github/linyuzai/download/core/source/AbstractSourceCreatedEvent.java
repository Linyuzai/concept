package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractSourceCreatedEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    public AbstractSourceCreatedEvent(DownloadContext context, @NonNull Source source) {
        this(context, source, "Create " + source.getDescription());
    }

    public AbstractSourceCreatedEvent(DownloadContext context, @NonNull Source source, String message) {
        super(context);
        this.source = source;
        setMessage(message);
    }
}
