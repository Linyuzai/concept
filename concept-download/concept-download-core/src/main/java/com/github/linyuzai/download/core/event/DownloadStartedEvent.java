package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;

public class DownloadStartedEvent extends DownloadContextEvent {

    public DownloadStartedEvent(DownloadContext context) {
        super(context);
    }
}
