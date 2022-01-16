package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public class SourceAlreadyLoadedEvent extends SourceLoadedEvent {

    public SourceAlreadyLoadedEvent(DownloadContext context, Source source) {
        super(context, source, "Skip load " + source + " because it has already been loaded");
    }
}
