package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressedEvent;
import com.github.linyuzai.download.core.context.AbstractContextDestroyedEvent;
import com.github.linyuzai.download.core.context.AbstractContextInitializedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.load.AbstractSourceLoadedEvent;
import com.github.linyuzai.download.core.source.AbstractSourceCreatedEvent;
import com.github.linyuzai.download.core.web.AbstractResponseWrittenEvent;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class StandardDownloadLogger extends DownloadLogger {

    @Override
    public void onEvent(Object event) {
        if (event instanceof DownloadContextEvent) {
            DownloadContextEvent dce = (DownloadContextEvent) event;
            DownloadContext context = dce.getContext();
            if (event instanceof AbstractContextInitializedEvent ||
                    event instanceof AbstractSourceCreatedEvent ||
                    event instanceof AbstractSourceLoadedEvent ||
                    event instanceof AbstractSourceCompressedEvent ||
                    event instanceof AbstractResponseWrittenEvent ||
                    event instanceof AbstractContextDestroyedEvent) {
                log(context, dce.getMessage());
            }
        }
    }
}
