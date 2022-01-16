package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;

public class ResponseWrittenEvent extends DownloadContextEvent {

    public ResponseWrittenEvent(DownloadContext context) {
        super(context);
        setMessage("Response written");
    }
}
