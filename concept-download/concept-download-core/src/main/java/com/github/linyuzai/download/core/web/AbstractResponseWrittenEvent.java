package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;

public class AbstractResponseWrittenEvent extends DownloadContextEvent {

    public AbstractResponseWrittenEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
