package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.AbstractContextDestroyedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

@Getter
public class SourceReleasedEvent extends AbstractContextDestroyedEvent {

    private final Source source;

    public SourceReleasedEvent(DownloadContext context, Source source) {
        super(context, "Source resource released");
        this.source = source;
    }
}
