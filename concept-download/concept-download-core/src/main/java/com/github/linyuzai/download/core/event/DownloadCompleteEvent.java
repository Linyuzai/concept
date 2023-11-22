package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;

public class DownloadCompleteEvent extends DownloadContextEvent {

    public DownloadCompleteEvent(DownloadContext context) {
        super(context);
        setMessage("Context destroyed");
    }
}
