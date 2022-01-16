package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SourceCreatedEvent extends DownloadContextEvent {

    @NonNull
    private final Source source;

    public SourceCreatedEvent(DownloadContext context, @NonNull Source source) {
        super(context);
        this.source = source;
        setMessage("Create " + source);
    }
}
