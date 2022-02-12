package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

/**
 * {@link DownloadContext} 初始化相关的事件的父类。
 */
public class AbstractInitializeContextEvent extends DownloadContextEvent {

    public AbstractInitializeContextEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
