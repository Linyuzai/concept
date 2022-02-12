package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.compress.AbstractCompressSourceEvent;
import com.github.linyuzai.download.core.context.AbstractDestroyContextEvent;
import com.github.linyuzai.download.core.context.AbstractInitializeContextEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.load.AbstractLoadSourceEvent;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;
import com.github.linyuzai.download.core.web.AbstractWriteResponseEvent;

public class StandardDownloadLogger extends DownloadLogger {

    @Override
    public void onEvent(Object event) {
        if (event instanceof DownloadContextEvent) {
            DownloadContextEvent dce = (DownloadContextEvent) event;
            DownloadContext context = dce.getContext();
            if (event instanceof AbstractInitializeContextEvent ||
                    event instanceof AbstractCreateSourceEvent ||
                    event instanceof AbstractLoadSourceEvent ||
                    event instanceof AbstractCompressSourceEvent ||
                    event instanceof AbstractWriteResponseEvent ||
                    event instanceof AbstractDestroyContextEvent) {
                log(context, dce.getMessage());
            }
        }
    }
}
