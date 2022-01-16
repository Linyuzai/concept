package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;

@Getter
public class SourceReleasedEvent extends DownloadContextEvent {

    private final Source source;

    public SourceReleasedEvent(DownloadContext context, Source source) {
        super(context);
        this.source = source;
        setMessage("Source resource released");
    }
}
