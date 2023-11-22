package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;

public class DownloadStartEvent extends DownloadContextEvent {

    public DownloadStartEvent(DownloadContext context) {
        super(context);
        setMessage("Context initialized");
    }
}
