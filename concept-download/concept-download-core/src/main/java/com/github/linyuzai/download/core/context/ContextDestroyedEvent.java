package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

public class ContextDestroyedEvent extends DownloadContextEvent {

    public ContextDestroyedEvent(DownloadContext context) {
        super(context);
        setMessage("Context destroyed");
    }
}
