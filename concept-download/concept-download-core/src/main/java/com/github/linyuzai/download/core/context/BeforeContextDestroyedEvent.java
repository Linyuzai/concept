package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 销毁后会发布该事件。
 */
public class BeforeContextDestroyedEvent extends AbstractDestroyContextEvent {

    public BeforeContextDestroyedEvent(DownloadContext context) {
        super(context, "Context destroyed");
    }
}
