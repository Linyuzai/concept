package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;

/**
 * {@link DownloadResponse} 写入相关的事件的父类。
 */
public class AbstractWriteResponseEvent extends DownloadContextEvent {

    public AbstractWriteResponseEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
