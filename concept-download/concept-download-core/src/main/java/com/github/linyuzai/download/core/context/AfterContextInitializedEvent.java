package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 初始化后会发布该事件。
 */
public class AfterContextInitializedEvent extends AbstractInitializeContextEvent {

    public AfterContextInitializedEvent(DownloadContext context) {
        super(context, "Context initialized");
    }
}
