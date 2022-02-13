package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link DownloadResponse} 写入完成后会发布该事件。
 */
public class AfterResponseWrittenEvent extends AbstractWriteResponseEvent {

    public AfterResponseWrittenEvent(DownloadContext context) {
        super(context, "Response written");
    }
}
