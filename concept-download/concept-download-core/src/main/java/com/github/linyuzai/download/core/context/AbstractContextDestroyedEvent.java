package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

/**
 * {@link DownloadContext} 销毁相关的事件的父类
 * <p>
 * Super class of {@link DownloadContext} destroy related events
 */
public class AbstractContextDestroyedEvent extends DownloadContextEvent {

    public AbstractContextDestroyedEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
