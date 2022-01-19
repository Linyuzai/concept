package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

public class AbstractContextInitializedEvent extends DownloadContextEvent {

    public AbstractContextInitializedEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
