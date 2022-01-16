package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SourceLoadedEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    public SourceLoadedEvent(DownloadContext context, Source source) {
        this(context, source, "Load " + source);
    }

    public SourceLoadedEvent(DownloadContext context, @NonNull Source source, String message) {
        super(context);
        this.source = source;
        setMessage(message);
    }
}
