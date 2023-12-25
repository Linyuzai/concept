package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;

/**
 * {@link DownloadResponse} 写入完成后会发布该事件。
 */
public class ResponseWrittenEvent extends DownloadContextEvent {

    public ResponseWrittenEvent(DownloadContext context) {
        super(context);
    }
}
