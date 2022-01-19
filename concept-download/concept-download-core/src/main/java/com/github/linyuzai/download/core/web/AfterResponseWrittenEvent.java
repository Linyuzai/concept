package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;

public class AfterResponseWrittenEvent extends AbstractResponseWrittenEvent {

    public AfterResponseWrittenEvent(DownloadContext context) {
        super(context, "Response written");
    }
}
