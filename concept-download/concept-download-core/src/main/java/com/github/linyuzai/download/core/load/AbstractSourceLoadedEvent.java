package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractSourceLoadedEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    public AbstractSourceLoadedEvent(DownloadContext context, Source source) {
        this(context, source, "Load " + source.getDescription());
    }

    public AbstractSourceLoadedEvent(DownloadContext context, @NonNull Source source, String message) {
        super(context);
        this.source = source;
        setMessage(message);
    }
}
