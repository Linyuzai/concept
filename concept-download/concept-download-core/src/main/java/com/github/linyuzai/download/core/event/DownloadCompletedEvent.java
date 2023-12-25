package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;

public class DownloadCompletedEvent extends DownloadContextEvent {

    public DownloadCompletedEvent(DownloadContext context) {
        super(context);
    }
}
