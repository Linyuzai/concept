package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;

public class DownloadSuccessEvent extends DownloadContextEvent {

    public DownloadSuccessEvent(DownloadContext context) {
        super(context);
    }
}
