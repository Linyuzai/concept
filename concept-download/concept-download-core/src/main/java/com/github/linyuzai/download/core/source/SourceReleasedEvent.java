package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

@Getter
public class SourceReleasedEvent extends AbstractDestroyContextEvent {

    private final Source source;

    public SourceReleasedEvent(DownloadContext context, Source source) {
        super(context, "Source resource released");
        this.source = source;
    }
}
