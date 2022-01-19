package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

public class AbstractContextDestroyedEvent extends DownloadContextEvent {

    public AbstractContextDestroyedEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
