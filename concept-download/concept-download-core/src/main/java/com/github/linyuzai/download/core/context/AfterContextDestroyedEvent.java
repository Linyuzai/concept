package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 销毁后会发布该事件。
 * <p>
 * This event will be published after {@link DownloadContext} is destroyed.
 */
public class AfterContextDestroyedEvent extends AbstractContextDestroyedEvent {

    public AfterContextDestroyedEvent(DownloadContext context) {
        super(context, "Context destroyed");
    }
}
