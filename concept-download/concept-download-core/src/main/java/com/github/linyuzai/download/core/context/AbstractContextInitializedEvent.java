package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadContextEvent;

/**
 * {@link DownloadContext} 初始化相关的事件的父类
 * <p>
 * Super class of {@link DownloadContext} initialization related events
 */
public class AbstractContextInitializedEvent extends DownloadContextEvent {

    public AbstractContextInitializedEvent(DownloadContext context, String message) {
        super(context);
        setMessage(message);
    }
}
