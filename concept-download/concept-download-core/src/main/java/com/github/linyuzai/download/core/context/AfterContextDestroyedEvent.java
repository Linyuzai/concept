package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 销毁后会发布该事件。
 */
public class AfterContextDestroyedEvent extends AbstractDestroyContextEvent {

    public AfterContextDestroyedEvent(DownloadContext context) {
        super(context, "Context destroyed");
    }
}
