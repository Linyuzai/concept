package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

public class ContextInitializedEvent extends DownloadContextEvent {

    public ContextInitializedEvent(DownloadContext context) {
        super(context);
        setMessage("Context initialized");
    }
}
