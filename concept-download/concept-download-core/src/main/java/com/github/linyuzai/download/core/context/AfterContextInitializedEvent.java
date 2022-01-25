package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 初始化后会发布该事件。
 * <p>
 * This event will be published after {@link DownloadContext} is initialized.
 */
public class AfterContextInitializedEvent extends AbstractContextInitializedEvent {

    public AfterContextInitializedEvent(DownloadContext context) {
        super(context, "Context initialized");
    }
}
