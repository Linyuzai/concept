package com.github.linyuzai.download.core.context;

public class AfterContextInitializedEvent extends AbstractContextInitializedEvent {

    public AfterContextInitializedEvent(DownloadContext context) {
        super(context, "Context initialized");
    }
}
