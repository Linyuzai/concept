package com.github.linyuzai.download.core.context;

public class AfterContextDestroyedEvent extends AbstractContextDestroyedEvent {

    public AfterContextDestroyedEvent(DownloadContext context) {
        super(context, "Context destroyed");
    }
}
