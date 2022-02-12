package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

/**
 * {@link DownloadContext} 销毁相关的事件的父类
 */
public class AbstractDestroyContextEvent extends DownloadContextEvent {

    public AbstractDestroyContextEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
