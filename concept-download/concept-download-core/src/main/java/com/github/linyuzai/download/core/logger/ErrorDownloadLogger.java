package com.github.linyuzai.download.core.logger;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadErrorEvent;

public class ErrorDownloadLogger extends LoggingDownloadEventListener {

    @Override
    public void logOnEvent(Object event) {
        if (event instanceof DownloadErrorEvent) {
            DownloadContext context = ((DownloadErrorEvent) event).getContext();
            Throwable error = ((DownloadErrorEvent) event).getError();
            error(context, error);
        }
    }
}
